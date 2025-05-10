package dev.mfazio.repeattimer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.InlineSliderDefaults
import androidx.wear.compose.material.Text
import dev.mfazio.repeattimer.presentation.defaultTimerLength
import dev.mfazio.repeattimer.presentation.theme.JetBrainsMonoFontFamily

@Composable
fun TimerSettings(
    modifier: Modifier = Modifier,
    timerLength: Int = defaultTimerLength,
    onTimerLengthChange: (Int) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontFamily = JetBrainsMonoFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "Timer length: $timerLength seconds",
                fontSize = 12.sp,
                fontFamily = JetBrainsMonoFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            InlineSlider(
                value = timerLength,
                onValueChange = onTimerLengthChange,
                valueProgression = IntProgression.fromClosedRange(
                    rangeStart = 15,
                    rangeEnd = 90,
                    step = 15
                ),
                increaseIcon = { Icon(InlineSliderDefaults.Increase, "Increase") },
                decreaseIcon = { Icon(InlineSliderDefaults.Decrease, "Decrease") },
                segmented = true
            )
        }
    }
}