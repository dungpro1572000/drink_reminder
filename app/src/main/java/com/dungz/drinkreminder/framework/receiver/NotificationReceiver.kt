package com.dungz.drinkreminder.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.notification.NotificationData
import com.dungz.drinkreminder.utilities.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appRepository: AppRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        if (intent.action == AppConstant.NOTIFICATION_ACTION_RECEIVER) {
            val notificationId = intent.getIntExtra(AppConstant.NOTIFICATION_BUNDLE_ID, -1)
            if (notificationId < 0) return
            when (notificationId) {
                NotificationData.NOTIFICATION_ID_EXERCISE -> {}
                NotificationData.NOTIFICATION_ID_EYES_RELAX -> {}
                NotificationData.NOTIFICATION_ID_DRINK_WATER -> {
                    // Handle the drink water notification action
                    // You can add your logic here, such as updating a database or showing a dialog
                }

                else -> {
                    // Handle unknown notification actions if necessary
                }
            }
        }
    }
}