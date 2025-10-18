package com.cecc.carecompanion.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HelpScreen(nav: NavController) {
  val ctx = LocalContext.current
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Emergency & Help", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(8.dp))
    Button(onClick = {
      val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:108") }
      ctx.startActivity(intent)
    }) { Text("Call 108 Ambulance") }
    Spacer(Modifier.height(8.dp))
    Button(onClick = {
      val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:100") }
      ctx.startActivity(intent)
    }) { Text("Call Police 100") }
  }
}
