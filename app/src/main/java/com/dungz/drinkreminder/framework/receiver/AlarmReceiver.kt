package com.dungz.drinkreminder.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dungz.drinkreminder.framework.notification.AppNotificationManager
import com.dungz.drinkreminder.framework.notification.NotificationData
import com.dungz.drinkreminder.utilities.AppConstant

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (context == null) return
        if (intent.action == AppConstant.ALARM_ACTION_RECEIVER) {
            Log.d(
                "DungNT354", " get receiver from alarm" +
                        " ${intent.action} "
            )
            val notificationManager = AppNotificationManager(context)
            notificationManager.showNotification(
                context,
                NotificationData("Reminder", "Time to drink water!", AppConstant.ID_DRINK_WATER)
            )
        }
    }
}