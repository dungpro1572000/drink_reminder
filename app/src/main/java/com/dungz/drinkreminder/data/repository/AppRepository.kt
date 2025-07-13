package com.dungz.drinkreminder.data.repository

import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysDrink
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysExercise
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysEyesRelax
import com.dungz.drinkreminder.data.roomdb.model.RecordCompleteModel
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface AppRepository {

    // setter
    suspend fun setDrinkWaterInfo(drinkWaterModel: DrinkWaterModel)
    suspend fun setExerciseInfo(exerciseModel: ExerciseModel)
    suspend fun setEyeInfo(eyesMode: EyesMode)
    suspend fun setRecordComplete(recordCompleteModel: RecordCompleteModel)
    suspend fun setWorkTime(workingTimeModel: WorkingTimeModel)

    fun getRecordComplete(): Flow<List<RecordCompleteModel>>

    fun getDrinkWaterInfo(): Flow<DrinkWaterModel?>
    fun getExerciseInfo(): Flow<ExerciseModel?>
    fun getEyesInfo(): Flow<EyesMode?>

    fun getDrinkCount(): Flow<Int>
    fun getEyesRelaxCount(): Flow<Int>
    fun getExerciseCount(): Flow<Int>
    fun getAmountOfDay(): Flow<Int>

    fun updateDrinkChecked(isChecked: Boolean)
    fun updateExerciseChecked(isChecked: Boolean)
    fun updateEyesChecked(isChecked: Boolean)

    fun getDrinkNotificationStatus(): Flow<Boolean>
    fun getExerciseNotificationStatus(): Flow<Boolean>
    fun getEyesNotificationStatus(): Flow<Boolean>

    // Working time for schedule
    fun getMorningStartTime(): Flow<Date>
    fun getMorningEndTime(): Flow<Date>
    fun getAfternoonStartTime(): Flow<Date>
    fun getAfternoonEndTime(): Flow<Date>
    fun getRepeatDay(): Flow<List<Int>>
    fun getWorkingTime(): Flow<WorkingTime?>

    //Record
    fun getRecordByDate(date: String): Flow<RecordCompleteModel?>
    fun get5DayDrinkRecord(): Flow<List<Record5DaysDrink>>
    fun get5DayEyesRelaxRecord(): Flow<List<Record5DaysEyesRelax>>
    fun get5DayExerciseRecord(): Flow<List<Record5DaysExercise>>
    suspend fun updateRecordDrinkTime(date: String)
    suspend fun updateRecordEyesRelaxTime(date: String)
    suspend fun updateRecordExerciseTime(date: String)

    fun getDateDrinkCount(date: String): Flow<Int>
    fun getDateEyesRelaxCount(date: String): Flow<Int>
    fun getDateExerciseCount(date: String): Flow<Int>
}