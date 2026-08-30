package com.zhenya.dpichanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.material3.dynamicColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DpiChangerApp()
        }
    }
}

private val DPI_PRESETS = listOf(160, 180, 200, 220, 240, 260, 280, 300, 320)

@Composable
fun DpiChangerApp() {
    val context = LocalContext.current
    var currentDpi by remember { mutableStateOf(DensityUtils.getCurrentDensity(context)) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberScalingLazyListState()

    val colorScheme = try {
        dynamicColorScheme(context)
    } catch (e: Exception) {
        MaterialTheme.colorScheme
    }

    MaterialTheme(colorScheme = colorScheme) {
        TimeText()

        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Текущий DPI",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            item {
                Text(
                    text = "$currentDpi",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                statusMessage?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            items(DPI_PRESETS) { dpi ->
                val isCurrent = dpi == currentDpi
                Button(
                    onClick = {
                        DensityUtils.setDensity(dpi).fold(
                            onSuccess = {
                                currentDpi = dpi
                                statusMessage = "Применено: $dpi"
                            },
                            onFailure = { e ->
                                statusMessage = e.message ?: "Ошибка"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    colors = if (isCurrent) {
                        ButtonDefaults.buttonColors()
                    } else {
                        ButtonDefaults.filledTonalButtonColors()
                    }
                ) {
                    Text("$dpi dpi")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        DensityUtils.resetDensity().fold(
                            onSuccess = {
                                currentDpi = DensityUtils.getDefaultDensity(context)
                                statusMessage = "Сброшено на заводское"
                            },
                            onFailure = { e ->
                                statusMessage = e.message ?: "Ошибка"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Text("Сбросить")
                }
            }

            item {
                Text(
                    text = "Если ошибка прав — выполни на компьютере:\n" +
                            "adb shell pm grant com.zhenya.dpichanger " +
                            "android.permission.WRITE_SECURE_SETTINGS",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
