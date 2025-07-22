package com.dungz.drinkreminder.utilities

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs

fun Int.convertToDay(): String {
    return when (this) {
        1 -> "Sun"
        2 -> "Mon"
        3 -> "Tue"
        4 -> "Wed"
        5 -> "Thu"
        6 -> "Fri"
        7 -> "Sat"
        else -> ""
    }
}


fun convertHourMinuteToDate(hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return calendar.time
}

@OptIn(ExperimentalMaterial3Api::class)
fun formatTime(state: TimePickerState): String {
    val hour = state.hour
    val minute = state.minute
    return String.format(Locale.US, "%02d:%02d", hour, minute)
}

fun String.convertStringTimeToHHmm(): Date {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.parse(this)
}

fun minuteBetween2Date(start: String, end: String): Int {
    val startFormat = start.convertStringTimeToHHmm()
    val endFormat = end.convertStringTimeToHHmm()
    val diffInMillis = startFormat.time - endFormat.time
    return abs(diffInMillis / (1000 * 60)).toInt() // Convert milliseconds to minutes
}

fun String.convertTimeStringToInts(): Pair<Int?, Int?> {
    val parts = this.split(":")
    if (parts.size == 2) {
        try {
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            return Pair(hour, minute)
        } catch (e: NumberFormatException) {
            // Handle cases where conversion to Int fails (e.g., "ab:cd")
            return null to null
        }
    }
    // Handle invalid time string format
    return null to null
}

fun Date.formatToString(): String {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.format(this)
}

fun getTodayTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
}

fun formatLongToStringTime(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
}

//fun calcTimeLeft(timeString: String?, lastWorkingTime: String, nextTimeDuration: Int): Long {
//    return try {
//        val now = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//        val localTime = LocalTime.parse(timeString ?: "", formatter)
//        val todayTarget = now.toLocalDate().atTime(localTime)
//        val lastWorking = LocalDateTime.parse(lastWorkingTime, formatter)
//
//        val finalTarget = if (todayTarget.isBefore(now)) {
//            if (!lastWorking.isBefore(now)) {
//                todayTarget.plusDays(1)
//            } else {
//                todayTarget.plusMinutes(nextTimeDuration.toLong())
//            }
//        } else {
//            todayTarget
//        }
//        Log.d("DungNT354444"," calcTimeLeft: $finalTarget")
//        Duration.between(now, finalTarget).seconds
//    } catch (e: Exception) {
//        0L
//    }
//}
