package com.dungz.drinkreminder.framework.notification

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.RawRes

data class NotificationData(
    val title: String,
    val message: String,
    val channelId: Int,
    @DrawableRes val notificationIcon: Int
) {
    companion object {
        const val CHANNEL_ID_DRINK_WATER = "drink_water_channel"
        const val CHANNEL_ID_EYES_RELAX = "eyes_relax_channel"
        const val CHANNEL_ID_EXERCISE = "exercise_channel"

        const val NOTIFICATION_ID_DRINK_WATER = 1
        const val NOTIFICATION_ID_EYES_RELAX = 2
        const val NOTIFICATION_ID_EXERCISE = 3
    }
}
