package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecordComplete")
data class RecordCompleteEntity(
    @PrimaryKey()
    val date: String,
    val drinkTime: Int = 0,
    val eyesRelaxTime: Int = 0,
    val exerciseTime: Int = 0,
    val totalDrinkTime: Int = 0,
    val totalEyesRelaxTime: Int = 0,
    val totalExerciseTime: Int = 0,
)
