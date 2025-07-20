package com.dungz.drinkreminder.utilities

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalTime
import java.util.Locale


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
        val duration = if (now.isBefore(startTime)) {
            Duration.between(now, startTime).seconds
        } else {
            24 * 60 * 60 - Duration.between(startTime, now).seconds
        }
        val hour = duration / 60 / 60
        val minutes = (duration - hour * 60 * 60) / 60
        val seconds = duration - hour * 60 * 60 - minutes * 60

        val countdownText = String.format(Locale.US, "%02d:%02d:%02d", hour, minutes, seconds)
        emit(countdownText)
        delay(1000)
    }
}