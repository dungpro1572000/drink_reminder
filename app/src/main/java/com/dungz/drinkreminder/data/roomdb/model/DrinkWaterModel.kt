package com.dungz.drinkreminder.data.roomdb.model

data class DrinkWaterModel(
    val isNotificationOn: Boolean = true,
    val durationNotification: Int = 40
) {
}