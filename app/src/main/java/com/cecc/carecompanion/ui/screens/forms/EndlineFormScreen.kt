package com.cecc.carecompanion.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.cecc.carecompanion.RepositoryProvider
import com.cecc.carecompanion.data.local.entities.FormEntry
import kotlinx.serialization.json.*

@Composable
fun EndlineFormScreen(nav: NavController) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  val scrollState = rememberScrollState()

  var pid by remember { mutableStateOf("PID-001") }
  var baselineData by remember { mutableStateOf<JsonObject?>(null) }
  var midlineData by remember { mutableStateOf<JsonObject?>(null) }
  var isLoading by remember { mutableStateOf(false) }

  // Final assessment data
  var selfRatedHealth by remember { mutableStateOf("3") }
  var lifeSatisfaction by remember { mutableStateOf("3") }
  var socialSupport by remember { mutableStateOf("3") }
  var dailyActivities by remember { mutableStateOf("3") }
  var chronicConditions by remember { mutableStateOf("") }
  var medications by remember { mutableStateOf("") }
  var mobilityAid by remember { mutableStateOf("None") }
  var overallExperience by remember { mutableStateOf("") }
  var programSatisfaction by remember { mutableStateOf("3") }
  var recommendations by remember { mutableStateOf("") }

  // QALY Calculation
  val finalQalyScore = calculateQALY(
    age = 60,
    selfRatedHealth = selfRatedHealth.toIntOrNull() ?: 3,
    mobilityAid = mobilityAid,
    chronicConditions = chronicConditions
  )

  var message by remember { mutableStateOf("") }

  // Load baseline and midline data
  LaunchedEffect(pid) {
    isLoading = true
    try {
      val baseline = RepositoryProvider.db(ctx).formDao().latestByType(pid, "baseline")
      val midline = RepositoryProvider.db(ctx).formDao().latestByType(pid, "midline")

      baseline?.let {
        baselineData = Json.parseToJsonElement(it.dataJson).jsonObject
      }

      midline?.let {
        midlineData = Json.parseToJsonElement(it.dataJson).jsonObject
      }
    } catch (e: Exception) {
      // Handle error silently
    } finally {
      isLoading = false
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(scrollState)
  ) {
    Text("Endline Assessment Form", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
      value = pid,
      onValueChange = { pid = it },
      label = { Text("Participant ID") },
      modifier = Modifier.fillMaxWidth()
    )

    if (isLoading) {
      Spacer(Modifier.height(8.dp))
      CircularProgressIndicator()
      Spacer(Modifier.height(8.dp))
      Text("Loading previous assessment data...")
    }

    Spacer(Modifier.height(16.dp))

    // Final Health Status Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Final Health Status", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Text("Self-rated Health (1=Poor, 5=Excellent):")
        Slider(
          value = selfRatedHealth.toFloatOrNull() ?: 3f,
          onValueChange = { selfRatedHealth = it.toInt().toString() },
          valueRange = 1f..5f,
          steps = 3
        )
        Text("Current: $selfRatedHealth")

        Spacer(Modifier.height(8.dp))

        Text("Life Satisfaction (1=Very Dissatisfied, 5=Very Satisfied):")
        Slider(
          value = lifeSatisfaction.toFloatOrNull() ?: 3f,
          onValueChange = { lifeSatisfaction = it.toInt().toString() },
          valueRange = 1f..5f,
          steps = 3
        )
        Text("Current: $lifeSatisfaction")

        Spacer(Modifier.height(8.dp))

        Text("Social Support (1=Poor, 5=Excellent):")
        Slider(
          value = socialSupport.toFloatOrNull() ?: 3f,
          onValueChange = { socialSupport = it.toInt().toString() },
          valueRange = 1f..5f,
          steps = 3
        )
        Text("Current: $socialSupport")

        Spacer(Modifier.height(8.dp))

        Text("Daily Activities (1=Very Limited, 5=No Limitations):")
        Slider(
          value = dailyActivities.toFloatOrNull() ?: 3f,
          onValueChange = { dailyActivities = it.toInt().toString() },
          valueRange = 1f..5f,
          steps = 3
        )
        Text("Current: $dailyActivities")
      }
    }

    Spacer(Modifier.height(16.dp))

    // Final Health Information Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Final Health Information", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = chronicConditions,
          onValueChange = { chronicConditions = it },
          label = { Text("Final Chronic Conditions") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = medications,
          onValueChange = { medications = it },
          label = { Text("Final Medications") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = mobilityAid,
          onValueChange = { mobilityAid = it },
          label = { Text("Final Mobility Aid") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Program Evaluation Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Program Evaluation", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Text("Program Satisfaction (1=Very Dissatisfied, 5=Very Satisfied):")
        Slider(
          value = programSatisfaction.toFloatOrNull() ?: 3f,
          onValueChange = { programSatisfaction = it.toInt().toString() },
          valueRange = 1f..5f,
          steps = 3
        )
        Text("Current: $programSatisfaction")

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = overallExperience,
          onValueChange = { overallExperience = it },
          label = { Text("Overall Experience with CECC Program") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = recommendations,
          onValueChange = { recommendations = it },
          label = { Text("Recommendations for Program Improvement") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Complete QALY Trajectory Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Complete Health Utility Trajectory", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        val baselineQaly = baselineData?.get("qaly_score")?.jsonPrimitive?.doubleOrNull ?: 0.7
        val midlineQaly = midlineData?.get("current_qaly")?.jsonPrimitive?.doubleOrNull ?: baselineQaly
        val totalQalyChange = finalQalyScore - baselineQaly

        Text(
          text = "Baseline QALY: ${String.format("%.3f", baselineQaly)}",
          style = MaterialTheme.typography.bodyMedium
        )
        Text(
          text = "Midline QALY: ${String.format("%.3f", midlineQaly)}",
          style = MaterialTheme.typography.bodyMedium
        )
        Text(
          text = "Final QALY: ${String.format("%.3f", finalQalyScore)}",
          style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
          text = "Total QALY Gained: ${String.format("%.3f", totalQalyChange)}",
          style = MaterialTheme.typography.headlineSmall,
          color = if (totalQalyChange >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(Modifier.height(4.dp))
        Text(
          text = "Overall Impact: ${interpretTotalQALYChange(totalQalyChange)}",
          style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))
        Text(
          text = "Annualized QALY Gain: ${String.format("%.3f", totalQalyChange)} per year",
          style = MaterialTheme.typography.bodySmall
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    Button(
      onClick = {
        scope.launch {
          try {
            val formData = buildJsonObject {
              put("pid", pid)
              put("baseline_qaly", baselineData?.get("qaly_score")?.jsonPrimitive?.doubleOrNull ?: 0.0)
              put("midline_qaly", midlineData?.get("current_qaly")?.jsonPrimitive?.doubleOrNull ?: 0.0)
              put("final_qaly", finalQalyScore)
              put("total_qaly_change", finalQalyScore - (baselineData?.get("qaly_score")?.jsonPrimitive?.doubleOrNull ?: 0.0))
              put("self_rated_health", selfRatedHealth.toIntOrNull() ?: 3)
              put("life_satisfaction", lifeSatisfaction.toIntOrNull() ?: 3)
              put("social_support", socialSupport.toIntOrNull() ?: 3)
              put("daily_activities", dailyActivities.toIntOrNull() ?: 3)
              put("chronic_conditions", chronicConditions)
              put("medications", medications)
              put("mobility_aid", mobilityAid)
              put("program_satisfaction", programSatisfaction.toIntOrNull() ?: 3)
              put("overall_experience", overallExperience)
              put("recommendations", recommendations)
              put("completion_timestamp", System.currentTimeMillis())
            }

            RepositoryProvider.db(ctx).formDao().insert(
              FormEntry(
                participantId = pid,
                formType = "endline",
                dataJson = formData.toString(),
                createdAt = System.currentTimeMillis()
              )
            )

            message = "Endline assessment completed successfully"
          } catch (e: Exception) {
            message = "Error saving: ${e.message}"
          }
        }
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Complete Endline Assessment")
    }

    if (message.isNotEmpty()) {
      Spacer(Modifier.height(8.dp))
      Text(
        text = message,
        color = if (message.contains("Error"))
          MaterialTheme.colorScheme.error
        else
          MaterialTheme.colorScheme.primary
      )
    }

    Spacer(Modifier.height(16.dp))

    OutlinedButton(
      onClick = { nav.popBackStack() },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Back to Home")
    }
  }
}

private fun calculateQALY(age: Int, selfRatedHealth: Int, mobilityAid: String, chronicConditions: String): Double {
  // Base QALY calculation using age and self-rated health
  val ageAdjustment = when {
    age < 50 -> 1.0
    age < 60 -> 0.95
    age < 70 -> 0.90
    age < 80 -> 0.85
    else -> 0.80
  }

  val healthAdjustment = when (selfRatedHealth) {
    1 -> 0.3
    2 -> 0.5
    3 -> 0.7
    4 -> 0.85
    5 -> 1.0
    else -> 0.7
  }

  // Adjust for mobility aids
  val mobilityAdjustment = when (mobilityAid.lowercase()) {
    "none" -> 1.0
    "cane", "walking stick" -> 0.95
    "walker" -> 0.90
    "wheelchair" -> 0.85
    else -> 1.0
  }

  // Adjust for chronic conditions
  val conditionAdjustment = when {
    chronicConditions.contains("diabetes") -> 0.95
    chronicConditions.contains("hypertension") -> 0.96
    chronicConditions.contains("heart disease") -> 0.90
    chronicConditions.contains("multiple") -> 0.85
    else -> 1.0
  }

  return ageAdjustment * healthAdjustment * mobilityAdjustment * conditionAdjustment
}

private fun interpretTotalQALYChange(qalyChange: Double): String {
  return when {
    qalyChange > 0.2 -> "Excellent program impact - substantial health improvement"
    qalyChange > 0.1 -> "Good program impact - meaningful health improvement"
    qalyChange > 0.05 -> "Moderate program impact - noticeable health improvement"
    qalyChange > -0.05 -> "Minimal program impact - health status maintained"
    qalyChange > -0.1 -> "Concerning decline - program may need adjustment"
    else -> "Significant decline - program evaluation required"
  }
}
