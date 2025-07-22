package com.dungz.drinkreminder.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.framework.notification.NotificationData
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.getTodayTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    @IoDispatcher
    lateinit var dispatcher: CoroutineDispatcher
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        if (intent.action == AppConstant.NOTIFICATION_ACTION_RECEIVER) {
            val notificationId = intent.getIntExtra(AppConstant.NOTIFICATION_BUNDLE_ID, -1)
            if (notificationId < 0) return
            val pendingResult = goAsync()
            val coroutineScope = CoroutineScope(dispatcher)
            coroutineScope.launch {
                when (notificationId) {
                    NotificationData.NOTIFICATION_ID_EXERCISE -> {
                        appRepository.updateRecordDrinkTime(getTodayTime())
                    }

                    NotificationData.NOTIFICATION_ID_EYES_RELAX -> {
                        appRepository.updateRecordEyesRelaxTime(getTodayTime())
                    }

                    NotificationData.NOTIFICATION_ID_DRINK_WATER -> {
                        appRepository.updateRecordExerciseTime(getTodayTime())
                        // Handle the drink water notification action
                        // You can add your logic here, such as updating a database or showing a dialog
                    }

                    else -> {
                        Log.d("DungNT35444","update other record")
                    }
                }
            }
        }
    }
}