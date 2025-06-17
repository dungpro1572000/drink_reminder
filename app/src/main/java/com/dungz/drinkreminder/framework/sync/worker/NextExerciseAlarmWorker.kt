package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import com.dungz.drinkreminder.utilities.formatToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class NextExerciseAlarmWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val exerciseInfo = appRepository.getExerciseInfo().firstOrNull()
        val workingTime = appRepository.getWorkingTime().firstOrNull()
        if (exerciseInfo == null || workingTime == null) {
            return Result.failure()
        }
        val nextExerciseTime = exerciseInfo.nextNotificationTime.convertStringTimeToDate().apply {
            time + exerciseInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
        }
        val afternoonEndTime = workingTime.afternoonEndTime.convertStringTimeToDate()
        if (nextExerciseTime.before(afternoonEndTime)) {
            appRepository.setExerciseInfo(
                exerciseInfo.copy(
                    nextNotificationTime = nextExerciseTime.formatToString()
                )
            )
            alarmScheduler.setupAlarmDate(nextExerciseTime, Intent().apply {
                action = AppConstant.ALARM_ACTION_RECEIVER
                `package` = AppConstant.packageName
                putExtra(AppConstant.EXERCISE_BUNDLE_ID, AppConstant.ID_EXERCISE)
            }, AppConstant.ID_EXERCISE)
            return Result.success()
        } else {
            val newDayTime = workingTime.morningStartTime.convertStringTimeToDate().apply {
                time + exerciseInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            appRepository.setExerciseInfo(
                exerciseInfo.copy(
                    nextNotificationTime = newDayTime.formatToString()
                )
            )
            alarmScheduler.setupAlarmDate(newDayTime, Intent().apply {
                action = AppConstant.ALARM_ACTION_RECEIVER
                `package` = AppConstant.packageName
                putExtra(AppConstant.EXERCISE_BUNDLE_ID, AppConstant.ID_EXERCISE)
            }, AppConstant.ID_EXERCISE)
            return Result.success()
        }
        return Result.failure()
    }
}