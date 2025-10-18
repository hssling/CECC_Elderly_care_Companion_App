package com.cecc.carecompanion.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.cecc.carecompanion.data.preferences.AppPreferences
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Composable
fun SettingsScreen(nav: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferences = remember { AppPreferences(context) }

    var staffPin by remember { mutableStateOf("") }
    var biometricEnabled by remember { mutableStateOf(false) }
    var consentAccepted by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var showConsentDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    // Observe preferences
    LaunchedEffect(Unit) {
        preferences.staffPin.collect { staffPin = it ?: "" }
    }

    LaunchedEffect(Unit) {
        preferences.biometricEnabled.collect { biometricEnabled = it }
    }

    LaunchedEffect(Unit) {
        preferences.consentAccepted.collect { consentAccepted = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Staff Access Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Staff Access",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Biometric Authentication:")
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = {
                            scope.launch {
                                preferences.setBiometricEnabled(it)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showPinDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Staff PIN")
                }

                if (staffPin.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "PIN: ${"*".repeat(staffPin.length)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Consent Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Privacy & Consent",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Consent Accepted:")
                    Switch(
                        checked = consentAccepted,
                        onCheckedChange = {
                            scope.launch {
                                preferences.setConsentAccepted(it)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showConsentDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Consent Form")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showPrivacyDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Privacy Policy")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sync Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Data Sync",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { nav.navigate("sync") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Configure Sync")
                }
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

    // PIN Dialog
    if (showPinDialog) {
        var pinInput by remember { mutableStateOf("") }
        var confirmPin by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Set Staff PIN") },
            text = {
                Column {
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Enter PIN") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { confirmPin = it.filter { char -> char.isDigit() } },
                        label = { Text("Confirm PIN") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput == confirmPin && pinInput.length >= 4) {
                            scope.launch {
                                preferences.setStaffPin(pinInput)
                            }
                            showPinDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Consent Dialog
    if (showConsentDialog) {
        AlertDialog(
            onDismissRequest = { showConsentDialog = false },
            title = { Text("Consent Form") },
            text = {
                Text("By using this app, you agree to the collection and storage of assessment data for research purposes. All data is kept confidential and used only for elder care support programs. You can request data deletion at any time.")
            },
            confirmButton = {
                Button(onClick = { showConsentDialog = false }) {
                    Text("Accept")
                }
            }
        )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy") },
            text = {
                Text("We collect only necessary assessment data to provide elder care support. Data is stored locally on your device and optionally synced to secure cloud storage. We do not share personal information with third parties without consent.")
            },
            confirmButton = {
                Button(onClick = { showPrivacyDialog = false }) {
                    Text("Understood")
                }
            }
        )
    }
}
