package com.dungz.drinkreminder.data.repository

import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesModel
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysDrink
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysExercise
import com.dungz.drinkreminder.data.roomdb.model.Record5DaysEyesRelax
import com.dungz.drinkreminder.data.roomdb.model.RecordCompleteModel
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val appDb: AppDatabase) : AppRepository {

    override suspend fun setDrinkWaterInfo(drinkWaterModel: DrinkWaterModel) {
        appDb.drinkDao().insertDrinkWater(
            DrinkWaterEntity(
                isNotificationOn = drinkWaterModel.isNotificationOn,
                durationNotification = drinkWaterModel.durationNotification,
                nextNotificationTime = drinkWaterModel.nextNotificationTime,
                isChecked = drinkWaterModel.isChecked
            )
        )
    }

    override suspend fun setExerciseInfo(exerciseModel: ExerciseModel) {
        appDb.exerciseDao().insert(
            ExerciseEntity(
                isNotificationOn = exerciseModel.isNotificationOn,
                durationNotification = exerciseModel.durationNotification,
                nextNotificationTime = exerciseModel.nextNotificationTime,
                isChecked = exerciseModel.isChecked
            )
        )
    }

    override suspend fun setEyeInfo(eyesModel: EyesModel) {
        appDb.eyesDao().insert(
            EyesEntity(
                isNotificationOn = eyesModel.isNotificationOn,
                durationNotification = eyesModel.durationNotification,
                nextNotificationTime = eyesModel.nextNotificationTime,
                isChecked = eyesModel.isChecked
            )
        )
    }

    override suspend fun setRecordComplete(recordCompleteModel: RecordCompleteModel) {
        appDb.recordCompleteDao().insertRecord(
            RecordCompleteEntity(
                date = recordCompleteModel.date,
                drinkTime = recordCompleteModel.drinkTime,
                eyesRelaxTime = recordCompleteModel.eyesRelaxTime,
                exerciseTime = recordCompleteModel.exerciseTime,
                totalDrinkTime = recordCompleteModel.totalDrinkTime,
                totalEyesRelaxTime = recordCompleteModel.totalEyesRelaxTime,
                totalExerciseTime = recordCompleteModel.totalExerciseTime,
            )
        )
    }

    override suspend fun setWorkTime(workingTimeModel: WorkingTimeModel) {
        appDb.workingTimeDao().insertWorkingTime(
            WorkingTime(
                startTime = workingTimeModel.startTime,
                endTime = workingTimeModel.endTime,
                repeatDay = workingTimeModel.repeatDay,
            )
        )
    }

    override fun getRecordComplete(): Flow<List<RecordCompleteModel>> {
        return appDb.recordCompleteDao().getRecordCompleteData().map { data ->
            data?.map { recordCompleteEntity ->
                RecordCompleteModel(
                    date = recordCompleteEntity.date,
                    drinkTime = recordCompleteEntity.drinkTime,
                    eyesRelaxTime = recordCompleteEntity.eyesRelaxTime,
                    exerciseTime = recordCompleteEntity.exerciseTime,
                    totalDrinkTime = recordCompleteEntity.totalDrinkTime,
                    totalEyesRelaxTime = recordCompleteEntity.totalEyesRelaxTime,
                    totalExerciseTime = recordCompleteEntity.totalExerciseTime
                )
            } ?: emptyList()
        }
    }

    override fun getDrinkWaterInfo(): Flow<DrinkWaterModel?> {
        return appDb.drinkDao().getDrinkInfo().map {
            it?.let { drinkWaterEntity ->
                DrinkWaterModel(
                    isNotificationOn = drinkWaterEntity.isNotificationOn,
                    durationNotification = drinkWaterEntity.durationNotification,
                    nextNotificationTime = drinkWaterEntity.nextNotificationTime,
                    isChecked = drinkWaterEntity.isChecked
                )
            }
        }
    }

    override fun getExerciseInfo(): Flow<ExerciseModel?> {
        return appDb.exerciseDao().getAllExercise().map {
            it?.let { exerciseEntity ->
                ExerciseModel(
                    isNotificationOn = exerciseEntity.isNotificationOn,
                    durationNotification = exerciseEntity.durationNotification,
                    nextNotificationTime = exerciseEntity.nextNotificationTime,
                    isChecked = exerciseEntity.isChecked
                )
            }
        }
    }

    override fun getEyesInfo(): Flow<EyesModel?> {
        return appDb.eyesDao().getAllEyes().map {
            it?.let { eyesEntity ->
                EyesModel(
                    isNotificationOn = eyesEntity.isNotificationOn,
                    durationNotification = eyesEntity.durationNotification,
                    nextNotificationTime = eyesEntity.nextNotificationTime,
                    isChecked = eyesEntity.isChecked
                )
            }
        }
    }

    override fun getDrinkCount(): Flow<Int> {
        return appDb.recordCompleteDao().getCountDrinkTime()
    }

    override fun getEyesRelaxCount(): Flow<Int> {
        return appDb.recordCompleteDao().getCountEyesRelaxTime()
    }

    override fun getExerciseCount(): Flow<Int> {
        return appDb.recordCompleteDao().getCountExerciseTime()
    }

    override fun getAmountOfDay(): Flow<Int> {
        return appDb.recordCompleteDao().getCountDays()
    }

    override fun updateDrinkChecked(isChecked: Boolean) {
        appDb.drinkDao().updateDrinkChecked(isChecked)
    }

    override fun updateExerciseChecked(isChecked: Boolean) {
        appDb.exerciseDao().updateExerciseChecked(isChecked)
    }

    override fun updateEyesChecked(isChecked: Boolean) {
        appDb.eyesDao().updateEyesChecked(isChecked)
    }

    override fun getDrinkNotificationStatus(): Flow<Boolean> {
        return appDb.drinkDao().getDrinkInfo().map {
            it?.isNotificationOn ?: false
        }
    }

    override fun getExerciseNotificationStatus(): Flow<Boolean> {
        return appDb.exerciseDao().getAllExercise().map {
            it?.isNotificationOn ?: false
        }
    }

    override fun getEyesNotificationStatus(): Flow<Boolean> {
        return appDb.eyesDao().getAllEyes().map {
            it?.isNotificationOn ?: false
        }
    }

    override fun getMorningStartTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.startTime?.convertStringTimeToHHmm() ?: Date().apply {
                time = 0 // Default to epoch if no time is set
            }
        }
    }

    override fun getMorningEndTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.endTime?.convertStringTimeToHHmm() ?: Date().apply {
                time = 0 // Default to epoch if no time is set
            }
        }
    }

    override fun getRepeatDay(): Flow<List<Int>> {
        return appDb.workingTimeDao().getWorkingTime().map {
            it?.repeatDay ?: emptyList()
        }
    }

    override fun getWorkingTime(): Flow<WorkingTime?> {
        return appDb.workingTimeDao().getWorkingTime()
    }

    override suspend fun insertRecord(recordCompleteEntity: RecordCompleteEntity) {
        appDb.recordCompleteDao().insertRecord(recordCompleteEntity)
    }

    override fun getRecordByDate(date: String): Flow<RecordCompleteModel?> {
        return appDb.recordCompleteDao().getRecordByDate(date).map {
            it?.let { recordCompleteEntity ->
                RecordCompleteModel(
                    date = recordCompleteEntity.date,
                    drinkTime = recordCompleteEntity.drinkTime,
                    eyesRelaxTime = recordCompleteEntity.eyesRelaxTime,
                    exerciseTime = recordCompleteEntity.exerciseTime,
                    totalDrinkTime = recordCompleteEntity.totalDrinkTime,
                    totalEyesRelaxTime = recordCompleteEntity.totalEyesRelaxTime,
                    totalExerciseTime = recordCompleteEntity.totalExerciseTime
                )
            }
        }
    }

    override fun get5DayDrinkRecord(): Flow<List<Record5DaysDrink>> {
        return appDb.recordCompleteDao().get5DayDrinkRecord()
    }

    override fun get5DayEyesRelaxRecord(): Flow<List<Record5DaysEyesRelax>> {
        return appDb.recordCompleteDao().get5DayEyesRelaxRecord()
    }

    override fun get5DayExerciseRecord(): Flow<List<Record5DaysExercise>> {
        return appDb.recordCompleteDao().get5DayExerciseRecord()
    }

    override suspend fun updateRecordDrinkTime(date: String) {
        appDb.recordCompleteDao().updateDrinkTime(date)
    }

    override suspend fun updateRecordEyesRelaxTime(date: String) {
        appDb.recordCompleteDao().updateEyesRelaxTime(date)
    }

    override suspend fun updateRecordExerciseTime(date: String) {
        appDb.recordCompleteDao().updateExerciseTime(date)
    }

    override fun updateTotalDrinkTime(totalDrinkTime: Int, date: String) {
        appDb.recordCompleteDao().updateOneDayDrinkTimes(date, totalDrinkTime)
    }

    override fun updateTotalEyesRelaxTime(totalEyesRelaxTime: Int, date: String) {
        appDb.recordCompleteDao().updateOneDayEyesRelaxTimes(date, totalEyesRelaxTime)
    }

    override fun updateTotalExerciseTime(totalExerciseTime: Int, date: String) {
        appDb.recordCompleteDao().updateOneDayExerciseTimes(date, totalExerciseTime)
    }

    override fun getDateDrinkCount(date: String): Flow<Int> {
        return appDb.recordCompleteDao().getDateDrinkCount(date)
    }

    override fun getDateEyesRelaxCount(date: String): Flow<Int> {
        return appDb.recordCompleteDao().getDateEyesRelaxCount(date)
    }

    override fun getDateExerciseCount(date: String): Flow<Int> {
        return appDb.recordCompleteDao().getDateExerciseCount(date)
    }
}