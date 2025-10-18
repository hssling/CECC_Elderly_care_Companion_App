package com.cecc.carecompanion.ui.screens.screeners

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cecc.carecompanion.RepositoryProvider
import com.cecc.carecompanion.data.local.entities.Screening
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.io.InputStream

@Serializable
data class ScreenerItem(
    val id: String,
    val text_key: String,
    val reverse: Boolean,
    var selectedOption: ScreenerOption? = null
)

@Serializable
data class ScreenerOption(
    val id: String,
    val label_key: String,
    val score: Int
)

@Serializable
data class ScreenerConfig(
    val id: String,
    val title: String,
    val items: List<ScreenerItem>,
    val options: List<ScreenerOption>,
    val scoring: ScreenerScoring
)

@Serializable
data class ScreenerScoring(
    val bands: List<ScreenerBand>
)

@Serializable
data class ScreenerBand(
    val min: Int,
    val max: Int,
    val label: String
)

@Composable
fun GenericScreenerScreen(
    nav: NavController,
    screenerId: String,
    screenerTitle: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var screenerConfig by remember { mutableStateOf<ScreenerConfig?>(null) }
    var participantId by remember { mutableStateOf("PID-001") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Load screener configuration from JSON
    LaunchedEffect(screenerId) {
        try {
            val config = loadScreenerConfig(context, screenerId)
            screenerConfig = config
        } catch (e: Exception) {
            message = "Error loading screener: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    screenerConfig?.let { config ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = config.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = participantId,
                onValueChange = { participantId = it },
                label = { Text("Participant ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(config.items) { item ->
                    ScreenerQuestionItem(
                        item = item,
                        options = config.options,
                        selectedOption = item.selectedOption,
                        onOptionSelected = { option ->
                            item.selectedOption = option
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score Display
            val totalScore = applyReverseScoring(config.items)
            val riskLevel = calculateRiskLevel(totalScore, config.scoring)

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Score: $totalScore",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Risk Level: $riskLevel",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (config.items.any { it.selectedOption == null }) {
                        message = "Please answer all questions"
                        return@Button
                    }

                    scope.launch {
                        try {
                            RepositoryProvider.db(context).screeningDao().insert(
                                Screening(
                                    participantId = participantId,
                                    type = config.id.uppercase(),
                                    score = totalScore,
                                    risk = riskLevel,
                                    createdAt = System.currentTimeMillis()
                                )
                            )
                            message = "Saved successfully"
                        } catch (e: Exception) {
                            message = "Error saving: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Assessment")
            }

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = if (message.contains("Error"))
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error loading screener configuration")
        }
    }
}

@Composable
fun ScreenerQuestionItem(
    item: ScreenerItem,
    options: List<ScreenerOption>,
    selectedOption: ScreenerOption?,
    onOptionSelected: (ScreenerOption) -> Unit
) {
    val context = LocalContext.current
    val text = getStringResource(context, item.text_key)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            options.forEach { option ->
                val optionLabel = getStringResource(context, option.label_key)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onOptionSelected(option) }
                ) {
                    RadioButton(
                        selected = selectedOption?.id == option.id,
                        onClick = { onOptionSelected(option) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = optionLabel,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private fun getStringResource(context: android.content.Context, key: String): String {
    return try {
        val resourceId = context.resources.getIdentifier(key, "string", context.packageName)
        if (resourceId != 0) {
            context.getString(resourceId)
        } else {
            // Fallback to key if resource not found
            key.replace("_", " ").replaceFirstChar { it.uppercase() }
        }
    } catch (e: Exception) {
        key.replace("_", " ").replaceFirstChar { it.uppercase() }
    }
}

private fun loadScreenerConfig(context: android.content.Context, screenerId: String): ScreenerConfig {
    val fileName = "screeners/$screenerId.json"
    val inputStream: InputStream = context.assets.open(fileName)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<ScreenerConfig>(jsonString)
}

private fun calculateRiskLevel(score: Int, scoring: ScreenerScoring): String {
    return scoring.bands.firstOrNull { score in it.min..it.max }?.label
        ?: "Unknown"
}

private fun applyReverseScoring(items: List<ScreenerItem>): Int {
    return items.sumOf { item ->
        val score = item.selectedOption?.score ?: 0
        if (item.reverse) {
            // Reverse scoring logic (e.g., for a 0-4 scale, 4 becomes 0, 3 becomes 1, etc.)
            // This assumes a max score of 4 for reversible items. Adjust if needed.
            4 - score
        } else {
            score
        }
    }
}
