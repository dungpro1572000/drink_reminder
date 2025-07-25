package com.dungz.drinkreminder.data.roomdb.model

data class EyesModel(
    val nextNotificationTime: String = "08:35",
    val isNotificationOn: Boolean = false,
    val durationNotification:Int = 35,
    val isChecked : Boolean = false,
)