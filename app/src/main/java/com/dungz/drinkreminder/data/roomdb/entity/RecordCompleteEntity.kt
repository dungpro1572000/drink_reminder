package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecordComplete")
data class RecordCompleteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val drinkTime: Int,
    val eyesRelaxTime: Int,
    val exerciseTime: Int,
    val totalDrinkTime: Int,
    val totalEyesRelaxTime: Int,
    val totalExerciseTime: Int,
)
