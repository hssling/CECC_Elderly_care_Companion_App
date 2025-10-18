package com.cecc.carecompanion.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomBar(nav: NavController) {
  NavigationBar {
    NavigationBarItem(selected = false, onClick = { nav.navigate("home") }, label = { Text("Home") }, icon = { Icon(Icons.Default.Home, null) })
    NavigationBarItem(selected = false, onClick = { nav.navigate("sessions") }, label = { Text("Sessions") }, icon = { Icon(Icons.Default.List, null) })
    NavigationBarItem(selected = false, onClick = { nav.navigate("tools") }, label = { Text("Tools") }, icon = { Icon(Icons.Default.Build, null) })
    NavigationBarItem(selected = false, onClick = { nav.navigate("help") }, label = { Text("Help") }, icon = { Icon(Icons.Default.Info, null) })
    NavigationBarItem(selected = false, onClick = { nav.navigate("learn") }, label = { Text("Learn") }, icon = { Icon(Icons.Default.School, null) })
  }
}

@Composable
fun FABRecord(nav: NavController) {
    FloatingActionButton(onClick = { nav.navigate("record") }) { Icon(Icons.Default.Add, contentDescription = "Record") }
}
