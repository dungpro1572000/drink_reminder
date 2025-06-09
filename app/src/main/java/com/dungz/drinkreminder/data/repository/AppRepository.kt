package com.dungz.drinkreminder.data.repository

import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
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

    fun getDrinkWaterInfo(): Flow<DrinkWaterEntity>
    fun getExerciseInfo(): Flow<ExerciseEntity>
    fun getEyesInfo(): Flow<EyesEntity>

    fun getDrinkCount(): Flow<Int>
    fun getEyesRelaxCount(): Flow<Int>
    fun getExerciseCount(): Flow<Int>
    fun getAmountOfDay(): Flow<Int>

    // Working time for schedule
    fun getMorningStartTime(): Flow<Date>
    fun getMorningEndTime(): Flow<Date>
    fun getAfternoonStartTime(): Flow<Date>
    fun getAfternoonEndTime(): Flow<Date>
    fun getRepeatDay(): Flow<List<Int>>
    fun getWorkingTime(): Flow<WorkingTimeModel>
}