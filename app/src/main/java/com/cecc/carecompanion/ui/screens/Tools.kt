package com.cecc.carecompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ToolsScreen(nav: NavController) {
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Assessment Tools", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(16.dp))

    // Mental Health & Wellbeing
    Text("Mental Health & Wellbeing", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/ucla3") }, modifier = Modifier.fillMaxWidth()) {
      Text("UCLA Loneliness Scale (3-item)")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/gds15") }, modifier = Modifier.fillMaxWidth()) {
      Text("Geriatric Depression Scale (15-item)")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/pss10") }, modifier = Modifier.fillMaxWidth()) {
      Text("Perceived Stress Scale (10-item)")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/whoqol") }, modifier = Modifier.fillMaxWidth()) {
      Text("WHOQOL-OLD Quality of Life (16-item)")
    }

    Spacer(Modifier.height(16.dp))

    // Functional Assessment
    Text("Functional Assessment", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/adliadl") }, modifier = Modifier.fillMaxWidth()) {
      Text("ADL/IADL Functional Assessment")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/dukeunc") }, modifier = Modifier.fillMaxWidth()) {
      Text("DUKE-UNC Social Support Scale")
    }

    Spacer(Modifier.height(16.dp))

    // Caregiver Assessment
    Text("Caregiver Assessment", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/zarit12") }, modifier = Modifier.fillMaxWidth()) {
      Text("Zarit Burden Interview (12-item)")
    }

    Spacer(Modifier.height(16.dp))

    // Community Assessment
    Text("Community Assessment", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("screener/socialcapital") }, modifier = Modifier.fillMaxWidth()) {
      Text("Social Capital Assessment")
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
