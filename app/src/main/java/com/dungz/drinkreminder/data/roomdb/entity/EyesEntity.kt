package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Eyes")
data class EyesEntity(
    @PrimaryKey()
    val id: Int = 0,
    val isNotificationOn: Boolean = false,
    val durationNotification:Int = 35
)