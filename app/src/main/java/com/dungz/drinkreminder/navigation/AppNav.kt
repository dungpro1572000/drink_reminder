package com.dungz.drinkreminder.navigation

import android.system.Os.close
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.dungz.drinkreminder.activity.MainActivityViewModel
import com.dungz.drinkreminder.navigation.Screen.Main
import com.dungz.drinkreminder.navigation.Screen.Splash
import com.dungz.drinkreminder.ui.main.MainScreen
import com.dungz.drinkreminder.ui.main.home.HomeScreen
import com.dungz.drinkreminder.ui.onboarding.OnboardingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.io.path.moveTo
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun AppNav(modifier: Modifier = Modifier, viewModel: MainActivityViewModel = viewModel()) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = Main.route
    ) {
        composable(route = Splash.route) {
            OnboardingScreen(viewModel) {
                navController.navigate(Main.route) {
                    launchSingleTop = true
                }
            }
        }

        composable(Main.route) {
            MainScreen(viewModel)
        }

    }
}

@Composable
fun DoubleWaveBackground(
    modifier: Modifier = Modifier,
    colorFront: Color = Color(0xFF2196F3),
    colorBack: Color = Color(0xFF90CAF9),
    amplitude1: Float = 30f,
    amplitude2: Float = 10f,
    speed1: Float = 0.12f,
    speed2: Float = 0.07f,
) {
    var phase1 by remember { mutableStateOf(0f) }
    var phase2 by remember { mutableStateOf(0f) }

    // Animate 2 pha sóng riêng biệt
    LaunchedEffect(Unit) {
        while (true) {
            phase1 += speed1
            phase2 += speed2
            delay(16) // ~60fps
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        fun drawWave(amplitude: Float, frequency: Float, phase: Float, color: Color) {
            val path = Path().apply {
                moveTo(0f, height)

                for (x in 0..width.toInt()) {
                    val y =
                        (amplitude * sin((x.toFloat() / width) * frequency * 2 * PI + phase)).toFloat()
                    lineTo(x.toFloat(), height / 2 + y)
                }

                lineTo(width, height)
                close()
            }

            drawPath(path, color = color)
        }

        // Lớp dưới (nền)
        drawWave(amplitude2, 1.2f, phase2, colorBack)

        // Lớp trên (trước)
        drawWave(amplitude1, 1.8f, phase1, colorFront)
    }
}

@Composable
fun WaterWaveBackground(
    modifier: Modifier = Modifier,
    waveColor: Color = Color(0xFF2196F3),
    amplitude: Float = 20f,     // độ cao sóng
    frequency: Float = 1.5f,    // tần số sóng
    phase: Float = 0f           // pha sóng, dùng để animate
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height)

            for (x in 0..width.toInt()) {
                val y =
                    (amplitude * sin((x.toFloat() / width) * frequency * 2 * PI + phase)).toFloat()
                lineTo(x.toFloat(), height / 2 + y)
            }

            lineTo(width, height)
            close()
        }

        drawPath(
            path = path,
            color = waveColor
        )
    }
}

@Composable
fun AnimatedWaterWaveBackground() {
    var phase by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            phase += 0.1f
            delay(16) // ~60 FPS
        }
    }

    DoubleWaveBackground(
        modifier = Modifier.fillMaxSize(),

        )
}


@Preview
@Composable
fun testSong() {
    Box(modifier = Modifier.fillMaxSize()) {
        DoubleWaveBackground(
            modifier = Modifier.fillMaxSize()
        )

        Text(
            "🌊 Xin chào từ đại dương!",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }
}