package com.dungz.drinkreminder.data.roomdb.model

data class DrinkWaterModel(
    val inComingAlarm: String = "08:40",
    val nextInComingAlarm: String = "08:00",
    val isNotificationOn: Boolean = true,
    val durationNotification: Int = 40,
    val isChecked: Boolean = false,
) {
}