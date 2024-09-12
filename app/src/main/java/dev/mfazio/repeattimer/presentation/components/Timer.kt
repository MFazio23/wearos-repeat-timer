package dev.mfazio.repeattimer.presentation.components

import android.os.Vibrator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import androidx.wear.tooling.preview.devices.WearDevices
import dev.mfazio.repeattimer.R
import dev.mfazio.repeattimer.presentation.WearApp
import dev.mfazio.repeattimer.presentation.defaultRestTime
import dev.mfazio.repeattimer.presentation.defaultTimerLength
import dev.mfazio.repeattimer.presentation.extras.singleVibrate
import dev.mfazio.repeattimer.presentation.theme.JetBrainsMonoFontFamily
import dev.mfazio.repeattimer.presentation.theme.TimerBlue
import dev.mfazio.repeattimer.presentation.theme.TimerBlueTranslucent
import dev.mfazio.repeattimer.presentation.theme.extraInfoGray

private fun timeRemainingString(timeRemaining: Int) = when {
    timeRemaining in 0..9 -> {
        "00:0$timeRemaining"
    }

    timeRemaining in 10..59 -> {
        "00:$timeRemaining"
    }

    timeRemaining < 0 -> {
        "-0:0${-timeRemaining}"
    }

    else -> "--:--"
}

@OptIn(ExperimentalFoundationApi::class) //For combinedClickable
@Composable
fun Timer(
    timeRemaining: Int,
    currentRound: Int,
    timerRunning: Boolean,
    currentStepCountText: String?,
    vibrator: Vibrator,
    onTimerRunningChange: (Boolean) -> Unit,
    onTimeRemainingChange: (Int) -> Unit,
    onCurrentRoundChange: (Int) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val leadingTextStyle = TimeTextDefaults.timeTextStyle(color = extraInfoGray)

    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            val (timeTextRef, timeRemainingTextRef, buttonRef, roundTextRef) = createRefs()
            TimeText(
                timeTextStyle = leadingTextStyle,
                modifier = Modifier
                    .constrainAs(timeTextRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                endCurvedContent = {
                    currentStepCountText?.let {
                        curvedText(
                            text = it,
                            style = CurvedTextStyle(leadingTextStyle)
                        )
                    }
                },
            )
            Text(
                text = timeRemainingString(timeRemaining),
                color = if (timeRemaining > 0) TimerBlue else TimerBlueTranslucent,
                fontSize = 24.sp,
                fontFamily = JetBrainsMonoFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(timeRemainingTextRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .combinedClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {},
                        onDoubleClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            val newTimeRemaining = if (timeRemaining < 0) {
                                0
                            } else if (timeRemaining <= 10) {
                                -defaultRestTime
                            } else {
                                timeRemaining - 10
                            }
                            onTimeRemainingChange(newTimeRemaining)
                        },
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onTimeRemainingChange(defaultTimerLength)
                        }
                    )
            )
            PlayPauseButton(
                modifier = Modifier
                    .constrainAs(buttonRef) {
                        top.linkTo(timeRemainingTextRef.bottom)
                        bottom.linkTo(roundTextRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(40.dp),
                timerRunning = timerRunning,
                onButtonClick = {
                    vibrator.singleVibrate(length = 100L)
                    onTimerRunningChange(!timerRunning)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onTimerRunningChange(true)
                    onTimeRemainingChange(defaultTimerLength)
                    onCurrentRoundChange(1)
                }
            )
            Text(
                text = stringResource(id = R.string.round_template, currentRound),
                color = TimerBlue,
                fontSize = 12.sp,
                fontFamily = JetBrainsMonoFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(roundTextRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(8.dp, 8.dp, 8.dp, 8.dp)
                    .combinedClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {},
                        onDoubleClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCurrentRoundChange(currentRound + 1)
                        },
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCurrentRoundChange(1)
                        }
                    )
            )
        }
    }
}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("11,234")
}