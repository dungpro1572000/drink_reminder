package com.dungz.drinkreminder.data.roomdb.model

import javax.inject.Inject

data class EyesMode(
    val isNotificationOn: Boolean = false,
    val durationNotification: Int = 35
) {
}