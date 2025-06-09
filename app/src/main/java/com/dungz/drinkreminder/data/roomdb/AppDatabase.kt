package com.dungz.drinkreminder.data.roomdb

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dungz.drinkreminder.data.roomdb.dao.DrinkDao
import com.dungz.drinkreminder.data.roomdb.dao.ExerciseDao
import com.dungz.drinkreminder.data.roomdb.dao.EyesDao
import com.dungz.drinkreminder.data.roomdb.dao.RecordCompleteDao
import com.dungz.drinkreminder.data.roomdb.dao.WorkingTimeDao
import com.dungz.drinkreminder.data.roomdb.entity.Converters
import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime

@Database(
    entities = [EyesEntity::class, ExerciseEntity::class, WorkingTime::class, RecordCompleteEntity::class, DrinkWaterEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]

)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eyesDao(): EyesDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun drinkDao(): DrinkDao
    abstract fun workingTimeDao(): WorkingTimeDao
    abstract fun recordCompleteDao(): RecordCompleteDao
}