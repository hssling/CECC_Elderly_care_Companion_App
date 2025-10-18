package com.cecc.carecompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(nav: NavController) {
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("CECC Care Companion", style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(16.dp))

    // Forms Section
    Text("Assessment Forms", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
      Button(onClick = { nav.navigate("form/baseline") }) { Text("Baseline") }
      Button(onClick = { nav.navigate("form/midline") }) { Text("Midline") }
      Button(onClick = { nav.navigate("form/endline") }) { Text("Endline") }
    }

    Spacer(Modifier.height(24.dp))

    // Tools Section
    Text("Tools & Assessment", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("sessions") }, modifier = Modifier.fillMaxWidth()) {
      Text("Start CECC Session")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("tools") }, modifier = Modifier.fillMaxWidth()) {
      Text("Open Tools & Screeners")
    }

    Spacer(Modifier.height(24.dp))

    // Support Section
    Text("Support & Information", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("help") }, modifier = Modifier.fillMaxWidth()) {
      Text("Emergency & Help")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("learn") }, modifier = Modifier.fillMaxWidth()) {
      Text("Learn Modules")
    }
    Spacer(Modifier.height(8.dp))
    Button(onClick = { nav.navigate("settings") }, modifier = Modifier.fillMaxWidth()) {
      Text("Settings")
    }

    Spacer(Modifier.height(16.dp))

    // App Info
    Text(
      text = "Version 1.0.0",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.align(Alignment.CenterHorizontally)
    )
  }
}
