package com.cecc.carecompanion.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class SessionModule(val id: String, val title: String, val duration: Int)

@Composable
fun SessionsScreen(nav: NavController) {
  val items = listOf(
    SessionModule("fall_prevention_basic","Fall Prevention (Basic)",20),
    SessionModule("chair_yoga","Chair Yoga",15),
    SessionModule("memory_games","Memory Games",15),
    SessionModule("caregiver_micro","Caregiver Micro-Training",15)
  )
  LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
    items(items){ m ->
      Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Column(Modifier.padding(16.dp)) {
          Text(m.title, style = MaterialTheme.typography.titleMedium)
          Text("Duration: ${m.duration} min")
          Spacer(Modifier.height(8.dp))
          Button(onClick = { /* TODO: open module */ }) { Text("Open") }
        }
      }
    }
  }
}
