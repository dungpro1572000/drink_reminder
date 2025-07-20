package com.dungz.drinkreminder.ui.main.home

data class HomeScreenState(
    val drinkTime: String?,
    val eyesRelaxTime: String?,
    val exerciseTime: String?,
    val drinkTimeLeft: Int?,
    val eyesRelaxTimeLeft: Int?,
    val exerciseTimeLeft: Int?,
    val isCheckedDrink: Boolean,
    val isCheckedEyes: Boolean,
    val isCheckedExercise: Boolean
) {
    companion object {
        val Default =
            HomeScreenState(
                drinkTimeLeft = 0, eyesRelaxTimeLeft = 0, exerciseTimeLeft = 0,
                drinkTime = "", eyesRelaxTime = "", exerciseTime = "",
                isCheckedDrink = false, isCheckedEyes = false, isCheckedExercise = false
            )
    }
}