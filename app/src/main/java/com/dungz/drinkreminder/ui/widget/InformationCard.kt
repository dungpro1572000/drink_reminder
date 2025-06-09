package com.dungz.drinkreminder.ui.widget

import android.graphics.RenderEffect
import android.graphics.Shader
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.whiteColor
import com.dungz.drinkreminder.utilities.countdownFlow
import java.time.LocalTime
import kotlin.math.sin
import kotlin.random.Random

enum class InformationCardType {
    DRINK, RELAX_EYES, EXERCISE
}

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    type: InformationCardType,
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
    Card(modifier = Modifier.clickable {
        onCardClick.invoke()
    }, shape = RoundedCornerShape(15.dp)) {
        Box(modifier = modifier) {
            Box(Modifier.align(Alignment.BottomCenter)) {
                when (type) {
                    InformationCardType.DRINK -> WaterBackgroundWithBetterWaves()
                    InformationCardType.RELAX_EYES -> GlowingGradientBackground()
                    InformationCardType.EXERCISE -> AnimatedHeartbeat()
                }
            }

            Box(
                Modifier
                    .align(Alignment.CenterEnd)
            ) {
                rightComponent.invoke()
            }
            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 12.dp)
            ) {
                Text(
                    text =
                        when (type) {
                            InformationCardType.DRINK -> stringResource(R.string.drink_incoming)
                            InformationCardType.EXERCISE -> stringResource(R.string.exercise_incoming)
                            InformationCardType.RELAX_EYES -> stringResource(R.string.eye_incoming)
                        }, style = TitleTextStyle
                )
                Spacer(Modifier.height(8.dp))
                Text("In coming $invokeTime", style = TitleTextStyle)
                Spacer(Modifier.height(8.dp))
                Text("${timeLeft.value}", style = NormalTextStyle)
            }

            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 24.dp)
            ) {
                inCardButton.invoke()
            }
        }
    }
}

@Composable
fun GlowingGradientBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradientAnimation")

    // Animate offset Y để tạo hiệu ứng ánh sáng chuyển động
    val offsetY = infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    val gradientColors = listOf(

        Color(0x00FFFFFF),
        Color(0xFFB3F68B), // vàng
        Color(0xFF93DC7A) // vàng nhạt// trong suốt
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                renderEffect = RenderEffect
                    .createBlurEffect(60f, 60f, Shader.TileMode.CLAMP)
                    .asComposeRenderEffect()
            }
            .drawWithCache {
                val brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = offsetY.value,
                    endY = size.height + offsetY.value
                )
                onDrawBehind {
                    drawRect(brush = brush)
                }
            }
    )
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

@Composable
fun AnimatedHeartbeat(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp),
    color: Color = Color.Blue,
    beatCount: Int = 10,
    speed: Int = 5000 // ms để lặp lại animation
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat_animation")
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(speed, easing = LinearEasing)
        ),
        label = "heartbeat_offset"
    )

    // Tạo danh sách các beat với chiều cao ngẫu nhiên (cache lại để không bị thay đổi mỗi recomposition)
    val beatHeights = remember {
        List(beatCount) { Random.nextFloat().coerceIn(0.3f, 1f) }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val spacing = width / beatCount

        val path = Path()
        val offsetX = animatedOffset.value * spacing

        path.moveTo(-offsetX, centerY)

        for (i in beatHeights.indices) {
            val x = i * spacing - offsetX
            val spikeHeight = beatHeights[i] * height / 2

            path.lineTo(x, centerY)
            path.lineTo(x + spacing / 4, centerY - spikeHeight)
            path.lineTo(x + spacing / 2, centerY + spikeHeight)
            path.lineTo(x + spacing * 3 / 4, centerY)
        }

        path.lineTo(width + spacing, centerY) // nối tới cuối màn

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}


@Preview
@Composable
fun tess() {
//    InformationCard(Modifier.height(160.dp), {}, startMinute = 20, startHour = 16, onTimeEnd = {})
    AnimatedHeartbeat()

}
