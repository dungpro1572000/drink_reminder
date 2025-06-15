package com.dungz.drinkreminder.data.roomdb.model

data class RecordCompleteModel(
    val date: String,
    val drinkTime: Int,
    val eyesRelaxTime: Int,
    val exerciseTime: Int,
    val totalDrinkTime: Int,
    val totalEyesRelaxTime: Int,
    val totalExerciseTime: Int,
) {
}