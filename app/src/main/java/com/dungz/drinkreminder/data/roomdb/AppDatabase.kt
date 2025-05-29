package com.dungz.drinkreminder.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dungz.drinkreminder.data.roomdb.dao.ExerciseDao
import com.dungz.drinkreminder.data.roomdb.dao.EyesDao
import com.dungz.drinkreminder.data.roomdb.dao.WorkingTimeDao
import com.dungz.drinkreminder.data.roomdb.model.Converters
import com.dungz.drinkreminder.data.roomdb.model.Exercise
import com.dungz.drinkreminder.data.roomdb.model.Eyes
import com.dungz.drinkreminder.data.roomdb.model.WorkingTime

@Database(entities = [Eyes::class, Exercise::class, WorkingTime::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eyesDao(): EyesDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workingTimeDao(): WorkingTimeDao
}