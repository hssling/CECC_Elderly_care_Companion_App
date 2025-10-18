package com.cecc.carecompanion.ui.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.cecc.carecompanion.engine.ScreenerRenderer
import java.nio.charset.Charset

@Composable
fun ScreenerHost(nav: NavController, screenerId: String){
  val ctx = LocalContext.current
  val assetName = when(screenerId){
    "ucla3" -> "screeners/ucla3.json"
    "gds15" -> "screeners/gds15.json"
    "zarit12" -> "screeners/zarit12.json"
    "pss10" -> "screeners/pss10.json"
    "adl_iadl" -> "screeners/adl_iadl.json"
    "duke_unc" -> "screeners/duke_unc.json"
    else -> "screeners/ucla3.json"
  }
  val json = ctx.assets.open(assetName).use { it.readBytes().toString(Charset.defaultCharset()) }
  ScreenerRenderer(configJson = json){ score, band ->
    // TODO: Save to Room Screening table
    nav.popBackStack()
  }
}
