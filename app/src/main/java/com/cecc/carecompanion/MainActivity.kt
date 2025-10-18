package com.cecc.carecompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cecc.carecompanion.ui.screens.*
import com.cecc.carecompanion.ui.screens.forms.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        val nav = rememberNavController()
        Scaffold(
          bottomBar = { BottomBar(nav) },
          floatingActionButton = { FABRecord(nav) }
        ) { padding ->
          NavHost(nav, startDestination = "home", modifier = Modifier.padding(padding)) {
            composable("home") { HomeScreen(nav) }
            composable("sessions") { SessionsScreen(nav) }
            composable("tools") { ToolsScreen(nav) }
            composable("help") { HelpScreen(nav) }
            composable("learn") { LearnScreen(nav) }
            composable("settings") { SettingsScreen(nav) }
            composable("sync") { SyncScreen(nav) }
            composable("record") { RecordScreen(nav) }

            composable("form/baseline") { BaselineFormScreen(nav) }
            composable("form/midline") { MidlineFormScreen(nav) }
            composable("form/endline") { EndlineFormScreen(nav) }

            composable("screener/{screenerId}") { backStackEntry ->
              val screenerId = backStackEntry.arguments?.getString("screenerId") ?: ""
              ScreenerHost(nav, screenerId)
            }
          }
        }
      }
    }
  }
}
