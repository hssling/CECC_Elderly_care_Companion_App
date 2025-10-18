package com.cecc.carecompanion.engine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ScreenerConfig(
    val id: String,
    val title: String,
    val items: List<Item>,
    val options: List<Option>,
    val scoring: Scoring
) {
    @Serializable
    data class Item(val id: String, val text_key: String, val reverse: Boolean = false)

    @Serializable
    data class Option(val id: String, val label_key: String, val score: Int)

    @Serializable
    data class Scoring(val bands: List<Band>) {
        @Serializable
        data class Band(val min: Int, val max: Int, val label: String)
    }
}

@Composable
fun ScreenerRenderer(configJson: String, onSave: (score: Int, band: String) -> Unit) {
    val cfg = Json.decodeFromString(ScreenerConfig.serializer(), configJson)
    val selections = remember { mutableStateMapOf<String, Int>() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(cfg.title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        cfg.items.forEach { item ->
            Text(item.text_key) // NOTE: Replace with stringResource via keys when localized
            Spacer(Modifier.height(4.dp))
            var selected by remember { mutableStateOf<Int?>(null) }
            cfg.options.forEach { opt ->
                Row(Modifier.fillMaxWidth()) {
                    RadioButton(selected = selected == opt.score, onClick = {
                        selected = opt.score
                        selections[item.id] = if (item.reverse) {
                            (cfg.options.maxOf { it.score } + cfg.options.minOf { it.score } - opt.score)
                        } else {
                            opt.score
                        }
                    })
                    Spacer(Modifier.width(8.dp)); Text(opt.label_key)
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        val total = selections.values.sum()
        val band = cfg.scoring.bands.firstOrNull { total in it.min..it.max }?.label ?: "NA"

        Text("Score: $total  |  Risk: $band")
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onSave(total, band) }) { Text("Save Result") }
    }
}
