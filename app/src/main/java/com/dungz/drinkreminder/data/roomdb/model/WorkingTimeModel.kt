package com.dungz.drinkreminder.data.roomdb.model

data class WorkingTimeModel(
    val morningStartTime: String,
    val morningEndTime: String,
    val afternoonStartTime: String,
    val afternoonEndTime: String,
    val repeatDay: List<Int>,
) {
}