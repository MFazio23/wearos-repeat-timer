package dev.mfazio.repeattimer.presentation.extras

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun getVibrator(context: Context) = if (isAtLeastS()) {
    val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    vibratorManager.defaultVibrator
} else {
    @Suppress("DEPRECATION")
    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}

fun Vibrator.singleVibrate(
    length: Long = 200L,
    effect: Int = VibrationEffect.DEFAULT_AMPLITUDE
) = this.vibrate(count = 1, length = length, effect = effect)

fun Vibrator.doubleVibrate(
    length: Long = 200L,
    pauseLength: Long = 100L,
    effect: Int = VibrationEffect.DEFAULT_AMPLITUDE
) = this.vibrate(count = 2, length = length, pauseLength = pauseLength, effect = effect)

fun Vibrator.vibrate(
    count: Int,
    length: Long = 200L,
    pauseLength: Long = 100L,
    effect: Int = VibrationEffect.DEFAULT_AMPLITUDE
) {
    this.vibrate(
        VibrationEffect.createWaveform(
            (1..count).flatMap { listOf(length, pauseLength) }.toLongArray(),
            (1..count).flatMap { listOf(effect, 0) }.toIntArray(),
            -1
        )
    )
}

fun isAtLeastS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S