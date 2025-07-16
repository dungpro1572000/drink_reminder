package com.dungz.drinkreminder.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.ui.theme.LocalBaseColorScheme

@Composable
fun Line(
    modifier: Modifier = Modifier,
    color: Color = LocalBaseColorScheme.current.borderColor,
    thickness: Float = 1f
) {
    Box(modifier = modifier.height(thickness.dp).fillMaxWidth().background(color))
}