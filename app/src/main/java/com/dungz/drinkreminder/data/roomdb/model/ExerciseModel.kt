package com.dungz.drinkreminder.data.roomdb.model

data class ExerciseModel(
    val nextNotificationTime: String = "09:00",
    val isNotificationOn: Boolean = false,
    val durationNotification: Int = 55
) {
}