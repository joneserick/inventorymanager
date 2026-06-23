package com.stefick.designsystem.ui.components.scanner

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_transition")
    val animatedLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_animation"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 0.99f }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val cutoutSize = canvasWidth * 0.7f
        val left = (canvasWidth - cutoutSize) / 2f
        val top = (canvasHeight - cutoutSize) / 2f
        val right = left + cutoutSize
        val bottom = top + cutoutSize

        drawRect(color = Color.Black.copy(alpha = 0.6f))

        drawRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(cutoutSize, cutoutSize),
            blendMode = BlendMode.Clear
        )

        val strokeWidth = 4.dp.toPx()
        val cornerLength = 32.dp.toPx()
        val primaryBlue = Color(0xFF2563EB) // Matches your design system

        // Top-Left corner
        drawLine(primaryBlue, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)
        drawLine(primaryBlue, Offset(left, top), Offset(left, top + cornerLength), strokeWidth)

        // Top-Right corner
        drawLine(primaryBlue, Offset(right, top), Offset(right - cornerLength, top), strokeWidth)
        drawLine(primaryBlue, Offset(right, top), Offset(right, top + cornerLength), strokeWidth)

        // Bottom-Left corner
        drawLine(primaryBlue, Offset(left, bottom), Offset(left + cornerLength, bottom), strokeWidth)
        drawLine(primaryBlue, Offset(left, bottom), Offset(left, bottom - cornerLength), strokeWidth)

        // Bottom-Right corner
        drawLine(primaryBlue, Offset(right, bottom), Offset(right - cornerLength, bottom), strokeWidth)
        drawLine(primaryBlue, Offset(right, bottom), Offset(right, bottom - cornerLength), strokeWidth)

        val laserY = top + (cutoutSize * animatedLineY)
        drawLine(
            color = Color.Red.copy(alpha = 0.8f),
            start = Offset(left, laserY),
            end = Offset(right, laserY),
            strokeWidth = 2.dp.toPx()
        )
    }
}