package com.stefick.designsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    background = BackgroundGray,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSurface = TextDark,
    error = ErrorRed
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}