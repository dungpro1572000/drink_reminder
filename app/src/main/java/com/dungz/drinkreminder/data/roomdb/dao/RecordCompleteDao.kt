package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysDrink
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysExercise
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysEyesRelax
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordCompleteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(record: RecordCompleteEntity)

    @Query("select drinkTime, totalDrinkTime, date from RecordComplete limit 5")
    fun get5DayDrinkRecord(): Flow<List<Record5DaysDrink>>

    @Query("select eyesRelaxTime, totalEyesRelaxTime, date from RecordComplete limit 5")
    fun get5DayEyesRelaxRecord(): Flow<List<Record5DaysEyesRelax>>

    @Query("select exerciseTime, totalExerciseTime, date from RecordComplete limit 5")
    fun get5DayExerciseRecord(): Flow<List<Record5DaysExercise>>

    @Query("select * from RecordComplete where date = :date")
    fun getRecordByDate(date: String): Flow<RecordCompleteEntity?>

    @Query("select SUM(drinkTime) from RecordComplete")
    fun getCountDrinkTime(): Flow<Int>

    @Query("update RecordComplete set drinkTime = drinkTime+1 where date = :date")
    suspend fun updateDrinkTime(date: String)

    @Query("select SUM(eyesRelaxTime) from RecordComplete")
    fun getCountEyesRelaxTime(): Flow<Int>


    @Query("select SUM(exerciseTime) from RecordComplete")
    fun getCountExerciseTime(): Flow<Int>

    @Query("update RecordComplete set eyesRelaxTime = (eyesRelaxTime+1) where date = :date")
    suspend fun updateEyesRelaxTime(date: String)

    @Query("update RecordComplete set exerciseTime = exerciseTime+1 where date = :date")
    suspend fun updateExerciseTime(date: String)

    @Query("select drinkTime from RecordComplete where date = :date")
    fun getDateDrinkCount(date: String): Flow<Int>

    @Query("select eyesRelaxTime from RecordComplete where date = :date")
    fun getDateEyesRelaxCount(date: String): Flow<Int>

    @Query("select exerciseTime from RecordComplete where date = :date")
    fun getDateExerciseCount(date: String): Flow<Int>

    @Query("select count(date) from RecordComplete")
    fun getCountDays(): Flow<Int>

    @Query("select * from RecordComplete")
    fun getRecordCompleteData(): Flow<List<RecordCompleteEntity>?>
}