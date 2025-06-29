package com.dungz.drinkreminder.ui.main.home

data class HomeScreenState(
    val drinkTimeLeft: Int,
    val eyesRelaxTimeLeft: Int,
    val exerciseTimeLeft: Int
) {
    companion object {
        val Default =
            HomeScreenState(drinkTimeLeft = 0, eyesRelaxTimeLeft = 0, exerciseTimeLeft = 0)
    }
}