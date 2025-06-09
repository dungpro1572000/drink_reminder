package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercise")
data class ExerciseEntity(
    @PrimaryKey
    val id:Int = 0,
    val isNotificationOn : Boolean = false,
    val durationNotification: Int = 55
)
