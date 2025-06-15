package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Eyes")
data class EyesEntity(
    @PrimaryKey()
    val id: Int = 0,
    val nextNotificationTime: String = "08:35",
    val isNotificationOn: Boolean = false,
    // Duration in minutes
    val durationNotification:Int = 35
) {
    companion object{
        val listDuration = listOf<Int>(35, 45, 55)
    }
}