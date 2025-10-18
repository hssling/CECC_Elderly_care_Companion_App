package com.cecc.carecompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LearnScreen(nav: NavController) {
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Learn Modules", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(8.dp))
    Text("Download and play offline videos, audio guides, and image cards.")
  }
}

@Composable
fun RecordScreen(nav: NavController) {
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Quick Record / Referral", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(8.dp))
    Text("Create incident, referral, or follow-up (TODO).")
  }
}
