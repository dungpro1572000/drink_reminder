package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DrinkWater")
data class DrinkWaterEntity(
    @PrimaryKey()
    val id: Int = 0,
    val nextNotificationTime: String = "08:40",
    val isNotificationOn: Boolean = true,
    // Duration in minutes
    val durationNotification: Int = 40
) {
    companion object {
        val listDuration = listOf<Int>(40, 50, 60)
    }
}
