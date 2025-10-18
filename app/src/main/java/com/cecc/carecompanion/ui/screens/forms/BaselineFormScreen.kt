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
import com.cecc.carecompanion.util.ValidationUtils
import kotlinx.serialization.json.*
import java.util.*

@Composable
fun BaselineFormScreen(nav: NavController) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  val scrollState = rememberScrollState()

  // Basic Information
  var pid by remember { mutableStateOf("PID-001") }
  var age by remember { mutableStateOf("") }
  var sex by remember { mutableStateOf("M") }
  var village by remember { mutableStateOf("Tumkur") }
  var education by remember { mutableStateOf("Primary") }
  var occupation by remember { mutableStateOf("Farmer") }
  var maritalStatus by remember { mutableStateOf("Married") }

  // Health Information
  var height by remember { mutableStateOf("") }
  var weight by remember { mutableStateOf("") }
  var bloodPressureSystolic by remember { mutableStateOf("") }
  var bloodPressureDiastolic by remember { mutableStateOf("") }
  var chronicConditions by remember { mutableStateOf("") }
  var medications by remember { mutableStateOf("") }
  var mobilityAid by remember { mutableStateOf("None") }

  // Socioeconomic Information
  var monthlyIncome by remember { mutableStateOf("") }
  var householdSize by remember { mutableStateOf("") }
  var housingType by remember { mutableStateOf("Pucca") }
  var toiletFacility by remember { mutableStateOf("Private") }
  var waterSource by remember { mutableStateOf("Piped") }

  // Quality of Life Indicators
  var selfRatedHealth by remember { mutableStateOf("3") }
  var lifeSatisfaction by remember { mutableStateOf("3") }
  var socialSupport by remember { mutableStateOf("3") }
  var dailyActivities by remember { mutableStateOf("3") }

  // QALY Calculation
  val qalyScore = calculateQALY(
    age = age.toIntOrNull() ?: 60,
    selfRatedHealth = selfRatedHealth.toIntOrNull() ?: 3,
    mobilityAid = mobilityAid,
    chronicConditions = chronicConditions
  )

  var message by remember { mutableStateOf("") }
  var showErrors by remember { mutableStateOf(false) }

  // Validation state
  val ageValidation = remember(age) { ValidationUtils.validateAge(age) }
  val heightValidation = remember(height) { ValidationUtils.validateHeight(height) }
  val weightValidation = remember(weight) { ValidationUtils.validateWeight(weight) }
  val bpValidation = remember(bloodPressureSystolic, bloodPressureDiastolic) {
    ValidationUtils.validateBloodPressure(bloodPressureSystolic, bloodPressureDiastolic)
  }
  val incomeValidation = remember(monthlyIncome) { ValidationUtils.validateIncome(monthlyIncome) }
  val householdValidation = remember(householdSize) { ValidationUtils.validateHouseholdSize(householdSize) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(scrollState)
  ) {
    Text("Baseline Assessment Form", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(16.dp))

    // Basic Information Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Basic Information", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = pid,
          onValueChange = { pid = it },
          label = { Text("Participant ID") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row {
          OutlinedTextField(
            value = age,
            onValueChange = { age = it.filter { ch -> ch.isDigit() } },
            label = { Text("Age") },
            modifier = Modifier.weight(1f)
          )
          Spacer(Modifier.width(8.dp))
          OutlinedTextField(
            value = sex,
            onValueChange = { sex = it },
            label = { Text("Sex (M/F/O)") },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = village,
          onValueChange = { village = it },
          label = { Text("Village/Ward") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row {
          OutlinedTextField(
            value = education,
            onValueChange = { education = it },
            label = { Text("Education") },
            modifier = Modifier.weight(1f)
          )
          Spacer(Modifier.width(8.dp))
          OutlinedTextField(
            value = occupation,
            onValueChange = { occupation = it },
            label = { Text("Occupation") },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = maritalStatus,
          onValueChange = { maritalStatus = it },
          label = { Text("Marital Status") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Health Information Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Health Information", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row {
          OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            modifier = Modifier.weight(1f)
          )
          Spacer(Modifier.width(8.dp))
          OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(Modifier.height(8.dp))

        Row {
          OutlinedTextField(
            value = bloodPressureSystolic,
            onValueChange = { bloodPressureSystolic = it },
            label = { Text("BP Systolic") },
            modifier = Modifier.weight(1f)
          )
          Spacer(Modifier.width(8.dp))
          OutlinedTextField(
            value = bloodPressureDiastolic,
            onValueChange = { bloodPressureDiastolic = it },
            label = { Text("BP Diastolic") },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = chronicConditions,
          onValueChange = { chronicConditions = it },
          label = { Text("Chronic Conditions") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = medications,
          onValueChange = { medications = it },
          label = { Text("Current Medications") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = mobilityAid,
          onValueChange = { mobilityAid = it },
          label = { Text("Mobility Aid Used") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Socioeconomic Information Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Socioeconomic Information", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = monthlyIncome,
          onValueChange = { monthlyIncome = it },
          label = { Text("Monthly Household Income (₹)") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = householdSize,
          onValueChange = { householdSize = it },
          label = { Text("Household Size") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = housingType,
          onValueChange = { housingType = it },
          label = { Text("Housing Type") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = toiletFacility,
          onValueChange = { toiletFacility = it },
          label = { Text("Toilet Facility") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
          value = waterSource,
          onValueChange = { waterSource = it },
          label = { Text("Drinking Water Source") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Quality of Life Indicators Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Quality of Life Indicators", style = MaterialTheme.typography.titleMedium)
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

    // QALY Score Display
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Health Utility Score (QALY)", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(
          text = "Estimated QALY: ${String.format("%.3f", qalyScore)}",
          style = MaterialTheme.typography.headlineSmall,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
          text = "Interpretation: ${interpretQALY(qalyScore)}",
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    Button(
      onClick = {
        showErrors = true

        // Validate all required fields
        val validationErrors = mutableListOf<String>()

        if (!ageValidation.isValid) {
          ageValidation.errorMessage?.let { validationErrors.add(it) }
        }
        if (!heightValidation.isValid) {
          heightValidation.errorMessage?.let { validationErrors.add(it) }
        }
        if (!weightValidation.isValid) {
          weightValidation.errorMessage?.let { validationErrors.add(it) }
        }
        if (!bpValidation.isValid) {
          bpValidation.errorMessage?.let { validationErrors.add(it) }
        }
        if (!incomeValidation.isValid) {
          incomeValidation.errorMessage?.let { validationErrors.add(it) }
        }
        if (!householdValidation.isValid) {
          householdValidation.errorMessage?.let { validationErrors.add(it) }
        }

        // If validation passes, save the data
        if (validationErrors.isEmpty()) {
          scope.launch {
            try {
              val formData = buildJsonObject {
                put("pid", pid)
                put("age", age.toIntOrNull() ?: 0)
                put("sex", sex)
                put("village", village)
                put("education", education)
                put("occupation", occupation)
                put("marital_status", maritalStatus)
                put("height", height.toFloatOrNull() ?: 0f)
                put("weight", weight.toFloatOrNull() ?: 0f)
                put("blood_pressure_systolic", bloodPressureSystolic.toIntOrNull() ?: 0)
                put("blood_pressure_diastolic", bloodPressureDiastolic.toIntOrNull() ?: 0)
                put("chronic_conditions", chronicConditions)
                put("medications", medications)
                put("mobility_aid", mobilityAid)
                put("monthly_income", monthlyIncome.toIntOrNull() ?: 0)
                put("household_size", householdSize.toIntOrNull() ?: 0)
                put("housing_type", housingType)
                put("toilet_facility", toiletFacility)
                put("water_source", waterSource)
                put("self_rated_health", selfRatedHealth.toIntOrNull() ?: 3)
                put("life_satisfaction", lifeSatisfaction.toIntOrNull() ?: 3)
                put("social_support", socialSupport.toIntOrNull() ?: 3)
                put("daily_activities", dailyActivities.toIntOrNull() ?: 3)
                put("qaly_score", qalyScore)
                put("timestamp", System.currentTimeMillis())
              }

              RepositoryProvider.db(ctx).formDao().insert(
                FormEntry(
                  participantId = pid,
                  formType = "baseline",
                  dataJson = formData.toString(),
                  createdAt = System.currentTimeMillis()
                )
              )

              message = "Baseline assessment saved successfully"
            } catch (e: Exception) {
              message = "Error saving: ${e.message}"
            }
          }
        } else {
          message = "Please fix the following errors:\n${validationErrors.joinToString("\n")}"
        }
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Save Baseline Assessment")
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

private fun interpretQALY(qaly: Double): String {
  return when {
    qaly >= 0.9 -> "Excellent health state"
    qaly >= 0.8 -> "Very good health state"
    qaly >= 0.7 -> "Good health state"
    qaly >= 0.6 -> "Moderate health state"
    qaly >= 0.5 -> "Poor health state"
    else -> "Very poor health state"
  }
}
