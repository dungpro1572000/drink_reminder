package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordCompleteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(record: RecordCompleteEntity)

    @Query("select SUM(drinkTime) from RecordComplete")
    fun getCountDrinkTime(): Flow<Int>

    @Query("select SUM(eyesRelaxTime) from RecordComplete")
    fun getCountEyesRelaxTime(): Flow<Int>

    @Query("select SUM(exerciseTime) from RecordComplete")
    fun getCountExerciseTime(): Flow<Int>

    @Query("select count(id) from RecordComplete")
    fun getCountDays(): Flow<Int>

    @Query("select * from RecordComplete")
    fun getRecordCompleteData(): Flow<RecordCompleteEntity>
}