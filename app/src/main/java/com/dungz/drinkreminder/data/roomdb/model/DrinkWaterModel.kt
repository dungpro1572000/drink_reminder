package com.dungz.drinkreminder.data.roomdb.model

data class DrinkWaterModel(
    val nextNotificationTime: String = "08:40",
    val isNotificationOn: Boolean = true,
    val durationNotification: Int = 40,
    val isChecked: Boolean = false,
) {
}