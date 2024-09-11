package dev.mfazio.repeattimer.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.fillMaxRectangle
import dev.mfazio.repeattimer.R
import dev.mfazio.repeattimer.presentation.components.PlayPauseButton
import dev.mfazio.repeattimer.presentation.components.Timer
import dev.mfazio.repeattimer.presentation.extras.doubleVibrate
import dev.mfazio.repeattimer.presentation.extras.getVibrator
import dev.mfazio.repeattimer.presentation.extras.singleVibrate
import dev.mfazio.repeattimer.presentation.theme.FazioRepeatTimerTheme
import dev.mfazio.repeattimer.presentation.theme.JetBrainsMonoFontFamily
import dev.mfazio.repeattimer.presentation.theme.TimerBlue
import dev.mfazio.repeattimer.presentation.theme.TimerBlueTranslucent
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            WearApp()
        }
    }
}

const val defaultTimerLength = 30
const val defaultRestTime = 5

@Composable
fun WearApp() {
    var timerRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(defaultTimerLength) }
    var currentRound by remember { mutableIntStateOf(1) }

    val vibrator = getVibrator(LocalContext.current)

    LaunchedEffect(timerRunning) {
        while (timerRunning) {
            delay(1000L)
            when {
                timeRemaining == 1 -> {
                    vibrator.doubleVibrate()
                    timeRemaining = -defaultRestTime
                }

                timeRemaining < -1 -> {
                    timeRemaining++
                }

                timeRemaining > 0 -> {
                    timeRemaining--
                }

                else -> {
                    vibrator.singleVibrate()
                    timeRemaining = defaultTimerLength
                    currentRound++
                }
            }
        }
    }


    FazioRepeatTimerTheme {
        Timer(
            timeRemaining = timeRemaining,
            currentRound = currentRound,
            timerRunning = timerRunning,
            vibrator = vibrator,
            onTimerRunningChange = { timerRunning = it },
            onTimeRemainingChange = { timeRemaining = it },
            onCurrentRoundChange = { currentRound = it }
        )
    }
}