package com.dungz.drinkreminder.data.roomdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Eyes")
data class Eyes(
    @PrimaryKey(autoGenerate = true,)
    val id: Int = 0,
    val isNoticed: Boolean = false,
    val timeActive: Double = 0.0,
)