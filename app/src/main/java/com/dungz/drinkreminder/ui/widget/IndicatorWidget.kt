package com.dungz.drinkreminder.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dungz.drinkreminder.ui.theme.LocalBaseColorScheme

@Composable
fun ListIndicatorWidget(currentIndex: Int = 0, index: Int = 3, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.width(90.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..index - 1) {
            IndicatorDetail(currentIndex == i)
        }
    }
}

@Composable
fun IndicatorDetail(isSelected: Boolean = false) {
    Box(
        modifier = Modifier
            .size(width = 24.dp, height = 6.dp)
            .border(width = 0.5.dp, shape = RoundedCornerShape(50.dp), color = LocalBaseColorScheme.current.indicatorColor)
            .background(if (isSelected) LocalBaseColorScheme.current.primaryButtonColor else LocalBaseColorScheme.current.indicatorColor),
    )
}