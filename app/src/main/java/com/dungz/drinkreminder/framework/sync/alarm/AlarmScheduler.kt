package com.dungz.drinkreminder.framework.sync.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.framework.receiver.AlarmReceiver
import com.dungz.drinkreminder.utilities.AppConstant
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(ioDispatcher + supervisorJob)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setupAlarmTimeMillis(
        timeMillis: Long = 5000,
        bundle: Bundle,
        requestCode: Int = AppConstant.ID_EYES_RELAX
    ) {
        // Create a PendingIntent to wrap the intent
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, AlarmReceiver::class.java).apply {
                    action = AppConstant.ALARM_ACTION_RECEIVER
                    `package` = AppConstant.packageName
                    putExtras(bundle)
                },
                PendingIntent.FLAG_MUTABLE
            )

//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, dateTime.hours)
//        calendar.set(Calendar.MINUTE, dateTime.minutes)

        // Set the alarm to trigger at the specified time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeMillis,
            pendingIntent
        )
    }

    suspend fun setupAlarmDate(
        dateTime: Date,
        bundle: Bundle,
        requestCode: Int = AppConstant.ID_EYES_RELAX
    ) {
        // Create a PendingIntent to wrap the intent
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, AlarmReceiver::class.java).apply {
                    action = AppConstant.ALARM_ACTION_RECEIVER
                    `package` = AppConstant.packageName
                    putExtras(bundle)
                },
                PendingIntent.FLAG_MUTABLE
            )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, dateTime.hours)
            set(Calendar.MINUTE, dateTime.minutes)
            if (timeInMillis <= System.currentTimeMillis()) {
                // If the time is in the past, set it to the next day
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        // Set the alarm to trigger at the specified time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}

fun Context.setUpAlarm(
    dateTime: Date = Date(),
    timeMillis: Long = 5000,
    intent: Intent,
    requestCode: Int = AppConstant.ID_EYES_RELAX,
) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Create a PendingIntent to wrap the intent
    val pendingIntent =
        PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE)
//    val calendar = Calendar.getInstance()
//    calendar.set(Calendar.HOUR_OF_DAY, dateTime.hours)
//    calendar.set(Calendar.MINUTE, dateTime.minutes)

    alarmManager.setExact(
        AlarmManager.ELAPSED_REALTIME,
        timeMillis,
        pendingIntent
    )

}