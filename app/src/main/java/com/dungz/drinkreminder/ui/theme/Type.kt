package com.dungz.drinkreminder.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BoldTitleTextStyle: TextStyle
    @Composable get() = TextStyle(
        color = LocalBaseColorScheme.current.whiteColor,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
    )

val TitleTextStyle: TextStyle
    @Composable get() = TextStyle(
        color = LocalBaseColorScheme.current.normalTextColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
    )

val TextInPrimaryButton: TextStyle
    @Composable get() = TextStyle(
        color = LocalBaseColorScheme.current.textInPrimaryButtonColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )

val NormalTextStyle: TextStyle
    @Composable get() = TextStyle(
        color = LocalBaseColorScheme.current.subTextColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
val InstructionTextStyle: TextStyle
    @Composable get() = TextStyle(
        color = LocalBaseColorScheme.current.instructionTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )