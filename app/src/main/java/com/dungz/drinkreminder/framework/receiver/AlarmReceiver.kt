package com.dungz.drinkreminder.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.framework.notification.NotificationData
import com.dungz.drinkreminder.framework.notification.NotificationManager
import com.dungz.drinkreminder.framework.sync.worker.NextDrinkAlarmWorker
import com.dungz.drinkreminder.framework.sync.worker.NextExerciseAlarmWorker
import com.dungz.drinkreminder.framework.sync.worker.NextEyesAlarmWorker
import com.dungz.drinkreminder.utilities.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    @IoDispatcher
    lateinit var dispatcher: CoroutineDispatcher

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (context == null) return
        if (intent.action == AppConstant.ALARM_ACTION_RECEIVER) {
            val coroutineScope = CoroutineScope(dispatcher)

            val notificationManager = NotificationManager(context)
            val data = intent.getIntExtra(AppConstant.ALARM_BUNDLE_ID, -1)
            if (data < 0) return
            coroutineScope.launch {
                val today = LocalDate.now()
                val dayOfWeek = today.dayOfWeek.value
                val workingTime = appRepository.getWorkingTime().firstOrNull()
                when (data) {
                    AppConstant.ID_DRINK_WATER -> {
                        Log.d(TAG, "Drink water alarm received")
                        val drinkNotificationStatus =
                            appRepository.getDrinkNotificationStatus().firstOrNull() ?: false
                        if (drinkNotificationStatus) {
                            if (workingTime != null && workingTime.repeatDay.contains(dayOfWeek)) {
                                notificationManager.showNotification(
                                    context, NotificationData(
                                        title = "Time to Drink Water",
                                        message = "It's time to hydrate yourself!",
                                        channelId = NotificationData.NOTIFICATION_ID_DRINK_WATER,
                                        notificationIcon = R.drawable.water_drop
                                    )
                                )
                            }
                        }
                        WorkManager.getInstance(context).enqueue(NextDrinkAlarmWorker.worker)
                    }

                    AppConstant.ID_EYES_RELAX -> {
                        Log.d(TAG, "Eyes relax alarm received")
                        // Handle eyes relax notification
                        val eyesNotificationStatus =
                            appRepository.getEyesNotificationStatus().firstOrNull() ?: false
                        if (eyesNotificationStatus) {
                            if (workingTime != null && workingTime.repeatDay.contains(dayOfWeek)) {
                                notificationManager.showNotification(
                                    context, NotificationData(
                                        title = "Time to Relax Your Eyes",
                                        message = "Take a break and relax your eyes!",
                                        channelId = NotificationData.NOTIFICATION_ID_EYES_RELAX,
                                        notificationIcon = R.drawable.icon_eyes
                                    )
                                )
                            }
                        }
                        WorkManager.getInstance(context).enqueue(NextEyesAlarmWorker.startWorker)
                    }

                    AppConstant.ID_EXERCISE -> {
                        Log.d(TAG, "Exercise alarm received")
                        // Handle exercise notification
                        val exerciseNotificationStatus =
                            appRepository.getExerciseNotificationStatus().firstOrNull() ?: false
                        if (exerciseNotificationStatus) {
                            if (workingTime != null && workingTime.repeatDay.contains(dayOfWeek)) {
                                notificationManager.showNotification(
                                    context, NotificationData(
                                        title = "Time to Exercise",
                                        message = "Get up and move your body!",
                                        channelId = NotificationData.NOTIFICATION_ID_EXERCISE,
                                        notificationIcon = R.drawable.heart_beat
                                    )
                                )
                            }
                        }
                        WorkManager.getInstance(context).enqueue(NextExerciseAlarmWorker.worker)
                    }

                    else -> {
                        Log.w(TAG, "Unknown alarm ID: $data")
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "AlarmReceiver"
    }
}