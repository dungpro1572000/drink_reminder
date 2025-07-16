package com.dungz.drinkreminder.ui.theme

import androidx.compose.ui.graphics.Color

interface BaseColorScheme {
    val primaryColor: Color
    val secondaryColor: Color
    val whiteColor: Color
    val blueBackgroundColor: Color
    val primaryButtonColor: Color
    val textInPrimaryButtonColor: Color
    val normalTextColor: Color
    val subTextColor: Color
    val borderColor: Color
    val instructionTextColor: Color
    val disableButtonColor: Color
    val indicatorColor: Color
}

object LightColorScheme : BaseColorScheme {
    override val primaryColor: Color
        get() = Color(0xFF5DCCFC)
    override val secondaryColor: Color
        get() = Color(0xFF9FDDFA)
    override val whiteColor: Color
        get() = Color(0xFFFFFFFF)
    override val blueBackgroundColor: Color
        get() = Color(0xFFF4F8FB)
    override val primaryButtonColor: Color
        get() = Color(0xFF5DCCFC)
    override val textInPrimaryButtonColor: Color
        get() = Color(0xFFFFFFFF)
    override val normalTextColor: Color
        get() = Color(0xFF141A1E)
    override val subTextColor: Color
        get() = Color(0xFF625D5D)
    override val borderColor: Color
        get() = Color(0xFFADADAD)
    override val instructionTextColor: Color
        get() = Color(0xFF90A5B4)
    override val disableButtonColor: Color
        get() = Color(0xFF767680)
    override val indicatorColor: Color
        get() = Color(0xFFF2F2F2)
}

object DarkColorScheme : BaseColorScheme {
    override val primaryColor: Color
        get() = Color(0xFF5DCCFC)
    override val secondaryColor: Color
        get() = Color(0xFF9FDDFA)
    override val whiteColor: Color
        get() = Color(0xFFFFFFFF)
    override val blueBackgroundColor: Color
        get() = Color(0xFFF4F8FB)
    override val primaryButtonColor: Color
        get() = Color(0xFF5DCCFC)
    override val textInPrimaryButtonColor: Color
        get() = Color(0xFFFFFFFF)
    override val normalTextColor: Color
        get() = Color(0xFF141A1E)
    override val subTextColor: Color
        get() = Color(0xFF625D5D)
    override val borderColor: Color
        get() = Color(0xFFADADAD)
    override val instructionTextColor: Color
        get() = Color(0xFF90A5B4)
    override val disableButtonColor: Color
        get() = Color(0xFF767680)
    override val indicatorColor: Color
        get() = Color(0xFFF2F2F2)
}