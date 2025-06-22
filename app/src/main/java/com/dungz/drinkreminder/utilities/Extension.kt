package com.dungz.drinkreminder.utilities

import android.icu.util.Calendar
import androidx.annotation.IntegerRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun String.convertStringTimeToDate(): Date {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.parse(this)
}

fun String.convertTimeStringToInts(): Pair<Int, Int> {
    val parts = this.split(":")
    if (parts.size == 2) {
        try {
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            return Pair(hour, minute)
        } catch (e: NumberFormatException) {
            // Handle cases where conversion to Int fails (e.g., "ab:cd")
            return 0 to 0
        }
    }
    // Handle invalid time string format
    return 0 to 0
}

fun Date.formatToString(): String {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.format(this)
}