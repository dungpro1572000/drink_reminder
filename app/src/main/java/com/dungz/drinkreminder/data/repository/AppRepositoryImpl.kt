package com.dungz.drinkreminder.data.repository

import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
import com.dungz.drinkreminder.data.roomdb.model.RecordCompleteModel
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import com.dungz.drinkreminder.utilities.convertHourMinuteToDate
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val appDb: AppDatabase) : AppRepository {

    override suspend fun setDrinkWaterInfo(drinkWaterModel: DrinkWaterModel) {
        appDb.drinkDao().insertDrinkWater(
            DrinkWaterEntity(
                isNotificationOn = drinkWaterModel.isNotificationOn,
                durationNotification = drinkWaterModel.durationNotification
            )
        )
    }

    override suspend fun setExerciseInfo(exerciseModel: ExerciseModel) {
        appDb.exerciseDao().insert(
            ExerciseEntity(
                isNotificationOn = exerciseModel.isNotificationOn,
                durationNotification = exerciseModel.durationNotification
            )
        )
    }

    override suspend fun setEyeInfo(eyesMode: EyesMode) {
        appDb.eyesDao().insert(
            EyesEntity(
                isNotificationOn = eyesMode.isNotificationOn,
                durationNotification = eyesMode.durationNotification
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
                morningStartTime = workingTimeModel.morningStartTime,
                morningEndTime = workingTimeModel.morningEndTime,
                afternoonStartTime = workingTimeModel.afternoonStartTime,
                afternoonEndTime = workingTimeModel.afternoonEndTime,
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
                    nextNotificationTime = drinkWaterEntity.nextNotificationTime
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
                    nextNotificationTime = exerciseEntity.nextNotificationTime
                )
            }
        }
    }

    override fun getEyesInfo(): Flow<EyesMode?> {
        return appDb.eyesDao().getAllEyes().map {
            it?.let { eyesEntity ->
                EyesMode(
                    isNotificationOn = eyesEntity.isNotificationOn,
                    durationNotification = eyesEntity.durationNotification,
                    nextNotificationTime = eyesEntity.nextNotificationTime,
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

    override fun getMorningStartTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.morningStartTime?.convertStringTimeToDate() ?: Date().apply {
                time = 0 // Default to epoch if no time is set
            }
        }
    }

    override fun getMorningEndTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.morningEndTime?.convertStringTimeToDate() ?: Date().apply {
                time = 0 // Default to epoch if no time is set
            }
        }
    }

    override fun getAfternoonStartTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.afternoonStartTime?.convertStringTimeToDate() ?: Date().apply {
                time = 0 // Default to epoch if no time is set
            }
        }
    }

    override fun getAfternoonEndTime(): Flow<Date> {
        val workingTime = appDb.workingTimeDao().getWorkingTime()
        return workingTime.map {
            it?.afternoonEndTime?.convertStringTimeToDate() ?: Date().apply {
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
}