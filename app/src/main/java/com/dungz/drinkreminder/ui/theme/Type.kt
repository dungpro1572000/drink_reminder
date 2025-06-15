package com.dungz.drinkreminder.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BoldTitleTextStyle = TextStyle(
    color = normalTextColor,
    fontSize = 24.sp,
    fontWeight = FontWeight.SemiBold,
)

val TitleTextStyle = TextStyle(
    color = normalTextColor,
    fontSize = 18.sp,
    fontWeight = FontWeight.Medium,
)

val TextInPrimaryButton = TextStyle(
    color = textInPrimaryButtonColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
)

val NormalTextStyle = TextStyle(
    color = subTextColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal
)
val InstructionTextStyle = TextStyle(
    color = instructionTextColor,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)