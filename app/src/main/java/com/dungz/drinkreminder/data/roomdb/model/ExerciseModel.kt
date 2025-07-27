package com.dungz.drinkreminder.data.roomdb.model

data class ExerciseModel(
    val inComingAlarm: String = "09:00",
    val nextInComingAlarm: String = "08:00",
    val isNotificationOn: Boolean = false,
    val durationNotification: Int = 55,
    val isChecked: Boolean = false,
)