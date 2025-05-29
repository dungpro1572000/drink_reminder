package com.dungz.drinkreminder.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.whiteColor
import com.dungz.drinkreminder.utilities.countdownFlow
import java.time.LocalTime
import kotlin.math.sin

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onTimeEnd: () -> Unit,
    startHour: Int,
    startMinute: Int,
    rightComponent: @Composable () -> Unit = {},
    inCardButton: @Composable () -> Unit = {},
) {
    val invokeTime = LocalTime.of(startHour, startMinute)
    val timeLeft = countdownFlow(startHour, startMinute).collectAsState(initial = 0)

    if (timeLeft.value == 0) {
        onTimeEnd.invoke()
    }
    Card(modifier.clickable {
        onCardClick.invoke()
    }, shape = RoundedCornerShape(15.dp)) {
        Box(modifier = modifier.background(whiteColor)) {
            WaterBackgroundWithBetterWaves()
            Box(Modifier.align(Alignment.CenterEnd).padding(end = 24.dp)) {
                rightComponent.invoke()
            }
            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 12.dp)
            ) {
                Text("$invokeTime", style = TitleTextStyle)
                Spacer(Modifier.height(8.dp))
                Text("${timeLeft.value}", style = NormalTextStyle)
            }

            Box(Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 24.dp)) {
                inCardButton.invoke()
            }
        }
    }
}

@Composable
fun WaterBackgroundWithBetterWaves() {
    val waveHeight = 70.dp
    val waveLength = 500.dp
    val density = LocalDensity.current

    val infiniteTransition = rememberInfiniteTransition(label = "betterWaveAnim")

    val phase1 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2 * Math.PI.toFloat(), animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "phase1"
    )

    val phase2 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2 * Math.PI.toFloat(), animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "phase2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val waveHeightPx = with(density) { waveHeight.toPx() }
        val waveLengthPx = with(density) { waveLength.toPx() }

        fun drawWave(phase: Float, amplitude: Float, color: Color) {
            val path = Path()
            path.moveTo(0f, height)

            for (x in 0..width.toInt()) {
                val y = (amplitude * sin((2 * Math.PI * x / waveLengthPx) + phase)).toFloat()
                path.lineTo(x.toFloat(), height - waveHeightPx + y)
            }

            path.lineTo(width, height)
            path.close()

            drawPath(path = path, color = color)
        }

        // Lớp sóng sau (nhẹ hơn)
        drawWave(
            phase = phase2.value,
            amplitude = waveHeightPx * 0.5f,
            color = Color(0xFF2196F3).copy(alpha = 0.4f)
        )

        // Lớp sóng trước (rõ hơn)
        drawWave(
            phase = phase1.value,
            amplitude = waveHeightPx * 0.8f,
            color = Color(0xFF2196F3).copy(alpha = 0.7f)
        )
    }
}


@Preview
@Composable
fun tess() {
    InformationCard(Modifier.height(160.dp), {}, startMinute = 20, startHour = 16, onTimeEnd = {})
}
