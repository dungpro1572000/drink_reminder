package com.dungz.drinkreminder.ui.widget

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ScaleComponent(
    modifier: Modifier = Modifier,
    targetScale: Float = 1.5f,
    content: @Composable () -> Unit,
    animationDuration: Int = 500
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(150.dp), // 👈 chọn size cụ thể
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
