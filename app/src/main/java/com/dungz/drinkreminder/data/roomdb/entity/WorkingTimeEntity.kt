package com.dungz.drinkreminder.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "WorkingTime")
data class WorkingTime(
    @PrimaryKey
    val id: Int = 0,
    val startTime: String,
    val endTime: String,
    val repeatDay: List<Int>,
)

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        return if (data.isEmpty()) emptyList() else data.split(",").map { it.toInt() }
    }
}