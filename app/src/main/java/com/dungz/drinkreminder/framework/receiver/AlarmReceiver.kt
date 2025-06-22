package com.dungz.drinkreminder.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dungz.drinkreminder.framework.notification.NotificationData
import com.dungz.drinkreminder.framework.notification.NotificationManager
import com.dungz.drinkreminder.utilities.AppConstant

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (context == null) return
        if (intent.action == AppConstant.ALARM_ACTION_RECEIVER) {
            val notificationManager = NotificationManager(context)
            val data = intent.getIntExtra(AppConstant.ALARM_BUNDLE_ID, -1)
            if (data < 0) return
            when (data) {
                AppConstant.ID_DRINK_WATER -> {
                    Log.d("AlarmReceiver", "Drink water alarm received")

                }

                AppConstant.ID_EYES_RELAX -> {
                    Log.d("AlarmReceiver", "Eyes relax alarm received")
                    // Handle eyes relax notification
                }

                AppConstant.ID_EXERCISE -> {
                    Log.d("AlarmReceiver", "Exercise alarm received")
                    // Handle exercise notification
                }

                else -> {
                    Log.w("AlarmReceiver", "Unknown alarm ID: $data")
                }
            }
        }
    }
}