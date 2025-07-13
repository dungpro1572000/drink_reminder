package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercise")
data class ExerciseEntity(
    @PrimaryKey
    val id:Int = 0,
    val nextNotificationTime: String = "09:00",
    val isNotificationOn : Boolean = false,
    // Duration in minutes
    val durationNotification: Int = 55,
    val isChecked: Boolean = false,
) {
    companion object{
        val listDuration = listOf<Int>(60, 65, 75)
    }
}
