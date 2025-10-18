package com.cecc.carecompanion.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cecc.carecompanion.data.preferences.AppPreferences
import com.cecc.carecompanion.remote.DriveUploader
import com.cecc.carecompanion.worker.SyncWorker
import kotlinx.coroutines.launch
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkInfo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SyncScreen(nav: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferences = remember { AppPreferences(context) }

    var syncEnabled by remember { mutableStateOf(false) }
    var lastSyncTimestamp by remember { mutableLongStateOf(0L) }
    var selectedFolderUri by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }
    var syncMessage by remember { mutableStateOf("") }

    val uploader = remember { DriveUploader(context) }

    // Observe preferences
    LaunchedEffect(Unit) {
        preferences.syncEnabled.collect { syncEnabled = it }
    }

    LaunchedEffect(Unit) {
        preferences.lastSyncTimestamp.collect { lastSyncTimestamp = it }
    }

    LaunchedEffect(Unit) {
        preferences.driveFolderUri.collect { selectedFolderUri = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Data Sync",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sync Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sync Status",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Sync Enabled:")
                    Switch(
                        checked = syncEnabled,
                        onCheckedChange = {
                            scope.launch {
                                preferences.setSyncEnabled(it)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Last Sync: ${
                        if (lastSyncTimestamp > 0) {
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                .format(Date(lastSyncTimestamp))
                        } else {
                            "Never"
                        }
                    }"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Drive Folder: ${
                        selectedFolderUri?.let { "Selected" } ?: "Not Selected"
                    }"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Drive Folder Selection
        if (syncEnabled) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Google Drive Setup",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val intent = uploader.createSelectFolderIntent()
                            (context as Activity).startActivityForResult(
                                intent,
                                DriveUploader.REQUEST_CODE_SELECT_FOLDER
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Drive Folder")
                    }

                    if (!selectedFolderUri.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Folder selected successfully!",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sync Now Button
            Button(
                onClick = {
                    if (selectedFolderUri.isNullOrEmpty()) {
                        syncMessage = "Please select a Drive folder first"
                        return@Button
                    }

                    isSyncing = true
                    syncMessage = "Syncing..."

                    // Trigger sync worker
                    val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                        .build()

                    WorkManager.getInstance(context).enqueue(syncWorkRequest)

                    // Observe work status
                    WorkManager.getInstance(context)
                        .getWorkInfoByIdLiveData(syncWorkRequest.id)
                        .observeForever { workInfo ->
                            when (workInfo.state) {
                                WorkInfo.State.SUCCEEDED -> {
                                    isSyncing = false
                                    syncMessage = "Sync completed successfully!"
                                    scope.launch {
                                        preferences.setLastSyncTimestamp(System.currentTimeMillis())
                                    }
                                }
                                WorkInfo.State.FAILED -> {
                                    isSyncing = false
                                    syncMessage = "Sync failed: ${workInfo.outputData.getString("message") ?: "Unknown error"}"
                                }
                                WorkInfo.State.RUNNING -> {
                                    isSyncing = true
                                    syncMessage = "Syncing..."
                                }
                                else -> {}
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSyncing && !selectedFolderUri.isNullOrEmpty()
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Sync Now")
            }

            if (syncMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = syncMessage,
                    color = if (syncMessage.contains("failed", true))
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Back Button
        OutlinedButton(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
