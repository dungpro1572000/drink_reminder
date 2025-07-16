package com.dungz.drinkreminder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val LocalBaseColorScheme = androidx.compose.runtime.staticCompositionLocalOf<BaseColorScheme> {
    error("DrinkColorScheme not provided")
}

@Composable
fun DrinkTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = if (isSystemInDarkTheme()) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    CompositionLocalProvider(LocalBaseColorScheme provides colorScheme) {
        content()
    }
}