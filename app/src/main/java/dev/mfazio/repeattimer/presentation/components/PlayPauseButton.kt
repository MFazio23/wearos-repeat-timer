package dev.mfazio.repeattimer.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import dev.mfazio.repeattimer.R
import dev.mfazio.repeattimer.presentation.theme.FazioRepeatTimerTheme
import dev.mfazio.repeattimer.presentation.theme.TimerBlue
import dev.mfazio.repeattimer.presentation.theme.TimerGreen
import dev.mfazio.repeattimer.presentation.theme.TimerRed

@Composable
fun PlayPauseButton(
    timerRunning: Boolean = false,
    onButtonClick: () -> Unit = {}
) {
    val description = "Play/Pause button"

    val iconModifier = Modifier
        .size(48.dp)
        .wrapContentSize(align = Alignment.Center)

    val buttonColor = if (timerRunning) {
        TimerRed
    } else {
        TimerGreen
    }

    val buttonIconResource = if (timerRunning) {
        R.drawable.pause_48px
    } else {
        R.drawable.play_arrow_48px
    }

    Box(modifier = Modifier.padding(4.dp)) {
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .border(6.dp, TimerBlue, shape = MaterialTheme.shapes.small)
                .aspectRatio(1F),

            colors = ButtonDefaults.buttonColors(
                backgroundColor = buttonColor
            ),
            onClick = onButtonClick
        ) {
            Icon(
                painterResource(id = buttonIconResource),
                contentDescription = description,
                modifier = iconModifier
            )
        }
    }
}

@Composable
@Preview
fun PlayButtonPreview() {
    FazioRepeatTimerTheme {
        Box(modifier = Modifier.height(100.dp)) {
            PlayPauseButton()
        }
    }
}