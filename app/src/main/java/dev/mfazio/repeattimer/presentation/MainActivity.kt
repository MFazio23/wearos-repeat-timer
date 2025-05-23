package dev.mfazio.repeattimer.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import com.google.android.horologist.compose.layout.AppScaffold
import dev.mfazio.repeattimer.presentation.components.Timer
import dev.mfazio.repeattimer.presentation.components.TimerSettings
import dev.mfazio.repeattimer.presentation.extras.doubleVibrate
import dev.mfazio.repeattimer.presentation.extras.getVibrator
import dev.mfazio.repeattimer.presentation.extras.singleVibrate
import dev.mfazio.repeattimer.presentation.theme.FazioRepeatTimerTheme
import dev.mfazio.repeattimer.presentation.theme.extraInfoGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val healthClient by lazy {
        HealthServices.getClient(this)
    }

    private val passiveMonitoringClient by lazy {
        healthClient.passiveMonitoringClient
    }

    private var currentStepsText by mutableStateOf<String?>(null)

    private lateinit var passiveListenerCallback: PassiveListenerCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val capabilities = passiveMonitoringClient.getCapabilitiesAsync().await()

                if (DataType.STEPS_DAILY in capabilities.supportedDataTypesPassiveGoals) {
                    val config = PassiveListenerConfig.Builder()
                        .setDataTypes(setOf(DataType.STEPS_DAILY))
                        .build()

                    passiveListenerCallback = object : PassiveListenerCallback {
                        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                            dataPoints.intervalDataPoints.firstOrNull()?.value?.let { currentSteps ->
                                currentStepsText = "%,d".format(currentSteps)

                            }
                        }
                    }

                    passiveMonitoringClient.setPassiveListenerCallback(
                        config,
                        passiveListenerCallback
                    )
                }
            }
        }

        setContent {
            WearApp(currentStepsText)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        passiveMonitoringClient.clearPassiveListenerCallbackAsync()
    }
}

const val defaultTimerLength = 30
const val defaultRestLength = 5

@Composable
fun WearApp(stepsText: String? = null) {
    var timerRunning by remember { mutableStateOf(false) }
    var timerLength by remember { mutableIntStateOf(defaultTimerLength) }
    var restLength by remember { mutableIntStateOf(defaultRestLength) }
    var timeRemaining by remember { mutableIntStateOf(timerLength) }

    var currentRound by remember { mutableIntStateOf(1) }

    val vibrator = getVibrator(LocalContext.current)
    val leadingTextStyle = TimeTextDefaults.timeTextStyle(color = extraInfoGray)

    LaunchedEffect(timerRunning) {
        while (timerRunning) {
            delay(1000L)
            when {
                timeRemaining == 1 -> {
                    vibrator.doubleVibrate()
                    timeRemaining = -restLength
                }

                timeRemaining < -1 -> {
                    timeRemaining++
                }

                timeRemaining > 0 -> {
                    timeRemaining--
                }

                else -> {
                    vibrator.singleVibrate()
                    timeRemaining = timerLength
                    currentRound++
                }
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = { 2 })

    FazioRepeatTimerTheme {
        AppScaffold(
            timeText = {
                TimeText(
                    timeTextStyle = leadingTextStyle,
                    endCurvedContent = {
                        stepsText?.let {
                            curvedText(
                                text = it,
                                style = CurvedTextStyle(leadingTextStyle)
                            )
                        }
                    },
                )
            }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (page == 0) {
                    Timer(
                        timerLength = timerLength,
                        restLength = restLength,
                        timeRemaining = timeRemaining,
                        currentRound = currentRound,
                        timerRunning = timerRunning,
                        vibrator = vibrator,
                        onTimerRunningChange = { timerRunning = it },
                        onTimeRemainingChange = { timeRemaining = it },
                        onCurrentRoundChange = { currentRound = it }
                    )
                } else {
                    TimerSettings(
                        timerLength = timerLength,
                        onTimerLengthChange = {
                            timerLength = it
                            if (!timerRunning) {
                                timeRemaining = it
                            }
                        },
                    )
                }
            }
        }

    }
}