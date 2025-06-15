package com.dungz.drinkreminder.framework.sync.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.utilities.AppConstant
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setupAlarmTimeMillis(
        timeMillis: Long = 5000,
        intent: Intent,
        requestCode: Int = AppConstant.ID_EYES_RELAX
    ) {
        // Create a PendingIntent to wrap the intent
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_MUTABLE)

//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, dateTime.hours)
//        calendar.set(Calendar.MINUTE, dateTime.minutes)

        // Set the alarm to trigger at the specified time
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeMillis,
            pendingIntent
        )
    }

    fun setupAlarmDate(
        dateTime: Date,
        intent: Intent,
        requestCode: Int = AppConstant.ID_EYES_RELAX
    ) {
        // Create a PendingIntent to wrap the intent
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_MUTABLE)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, dateTime.hours)
        calendar.set(Calendar.MINUTE, dateTime.minutes)

        // Set the alarm to trigger at the specified time
        alarmManager.setExact(
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