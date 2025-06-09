package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DrinkWater")
data class DrinkWaterEntity(
    @PrimaryKey()
    val id:Int =0 ,
    val isNotificationOn : Boolean = true,
    val durationNotification: Int = 40
)
