package com.dungz.drinkreminder.utilities

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


/*
    drink 20/30/40 min
    eyes 25/35/45 min
    exercise 55min
 */

fun countdownFlow(
    startHour: Int,
    startMinute: Int
): Flow<String> = flow {
    while (true) {
        val now = LocalTime.now()
        val startTime = LocalTime.of(startHour, startMinute)

        val secondsLeftSinceStart = Duration.between(startTime, now).seconds
        val duration = if (now.isBefore(startTime)) {
            Duration.between(now, startTime).seconds
        } else {
            Duration.ZERO.seconds // Nếu quá giờ, countdown về 0
        }
//        val intervalsPassed = if (minutesSinceStart >= 0) {
//            minutesSinceStart / intervalMinutes
//        } else {
//            -1
//        }
//
//        val nextTime = if (minutesSinceStart < 0) {
//            startTime
//        } else {
//            startTime.plusMinutes((intervalsPassed + 1) * intervalMinutes)
//        }
//
//        val duration = Duration.between(now, nextTime)
//        val minutesLeft = duration.toMinutes()
//        val secondsLeft = duration.seconds % 60
        val minutes = duration.minutes.inWholeMinutes
        val seconds = duration.seconds.inWholeSeconds

        val countdownText = String.format(Locale.US, "%02d:%02d", minutes, seconds)
//        emit(duration)
        delay(1000)
    }
}

fun convertHourMinuteToDate(hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return calendar.time
}

fun convertStringTimeToDate(time: String): Date {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.parse(time)
}