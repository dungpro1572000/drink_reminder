package com.dungz.drinkreminder.ui.widget

import androidx.annotation.IntegerRes
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieComponent(modifier: Modifier = Modifier, @RawRes resId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progression by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = progression
    )
}