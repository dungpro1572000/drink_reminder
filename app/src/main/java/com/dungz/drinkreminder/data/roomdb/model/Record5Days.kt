package com.dungz.drinkreminder.data.roomdb.model

data class Record5DaysDrink(
    val drinkTime: Int = 0,
    val totalDrinkTime: Int = 0,
    val date: String
)

data class Record5DaysEyesRelax(
    val eyesRelaxTime: Int = 0,
    val totalEyesRelaxTime: Int = 0,
    val date: String
)

data class Record5DaysExercise(
    val exerciseTime: Int = 0,
    val totalExerciseTime: Int = 0,
    val date: String
)
