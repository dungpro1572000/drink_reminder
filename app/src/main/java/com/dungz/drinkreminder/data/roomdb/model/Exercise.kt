package com.dungz.drinkreminder.data.roomdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercise")
data class Exercise(
    @PrimaryKey
    val id:Int = 0,
    val isNoticed : Boolean = false,
    val active: Double = 0.0
)
