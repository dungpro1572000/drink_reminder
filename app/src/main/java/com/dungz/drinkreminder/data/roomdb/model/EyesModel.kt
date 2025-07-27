package com.dungz.drinkreminder.data.roomdb.model

data class EyesModel(
    val inComingAlarm: String = "08:35",
    val nextInComingAlarm: String = "08:00",
    val isNotificationOn: Boolean = false,
    val durationNotification: Int = 35,
    val isChecked: Boolean = false,
)