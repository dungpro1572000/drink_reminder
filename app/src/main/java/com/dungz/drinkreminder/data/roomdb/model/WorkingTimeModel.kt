package com.dungz.drinkreminder.data.roomdb.model

data class WorkingTimeModel(
    val startTime: String,
    val endTime: String,
    val repeatDay: List<Int>,
) {
}