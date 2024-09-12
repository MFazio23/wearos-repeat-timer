package dev.mfazio.repeattimer.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

val TimerBlue = Color(0xFF2FA8CC)
val TimerBlueTranslucent = Color(0xFF2FA8CC).copy(alpha = 0.5F)
val TimerRed = Color(0xFFFF5364)
val TimerGreen = Color(0xFF53F953)

val extraInfoGray = Color(0x88A0A0A0)

internal val wearColorPalette: Colors = Colors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)