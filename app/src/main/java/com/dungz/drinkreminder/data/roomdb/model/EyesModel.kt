package com.dungz.drinkreminder.data.roomdb.model

import javax.inject.Inject

data class EyesMode(
    val nextNotificationTime: String = "08:35",
    val isNotificationOn: Boolean = false,
    val durationNotification:Int = 35,
    val isChecked : Boolean = false,
) {
}