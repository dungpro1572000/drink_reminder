package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.AppConstant.ID_DRINK_WATER
import com.dungz.drinkreminder.utilities.AppConstant.ID_EXERCISE
import com.dungz.drinkreminder.utilities.AppConstant.ID_EYES_RELAX
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

/// Calculate new day time for wakeup alarm.
/// new day time = workingTime.morningStartTime + durationNotification
@HiltWorker
class SetUpEveryDayWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val apiRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val dataWorkingTime = apiRepository.getWorkingTime().firstOrNull()
            val dataDrink = apiRepository.getDrinkWaterInfo().firstOrNull()
            val dateEyes = apiRepository.getEyesInfo().firstOrNull()
            val dataExercise = apiRepository.getExerciseInfo().firstOrNull()

            dataDrink ?: return Result.failure()
            dataExercise ?: return Result.failure()
            dateEyes ?: return Result.failure()
            dataWorkingTime ?: return Result.failure()

            // calculate time for alarm
            val drinkTime = dataWorkingTime.morningStartTime.convertStringTimeToHHmm().apply {
                time + dataDrink.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            val eyesRelaxTime = dataWorkingTime.morningStartTime.convertStringTimeToHHmm().apply {
                time + dateEyes.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            val exerciseTime = dataWorkingTime.morningStartTime.convertStringTimeToHHmm().apply {
                time + dataExercise.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
//         calculate next time for alarm
            val nextDrinkAlarm = drinkTime.apply {
                time + dataDrink.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            val nextEyeTime = eyesRelaxTime.apply {
                time + dateEyes.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            val nextExerciseTime = exerciseTime.apply {
                time + dataExercise.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            // Update the repository with the new next notification times
            apiRepository.setDrinkWaterInfo(dataDrink.copy(nextNotificationTime = nextDrinkAlarm.formatToString()))
            apiRepository.setEyeInfo(dateEyes.copy(nextNotificationTime = nextEyeTime.formatToString()))
            apiRepository.setExerciseInfo(dataExercise.copy(nextNotificationTime = nextExerciseTime.formatToString()))
            // Setup alarms
            alarmScheduler.setupAlarmDate(
                dateTime = drinkTime, Bundle().apply {
                    putInt(AppConstant.ALARM_BUNDLE_ID, ID_DRINK_WATER)
                },
                requestCode = ID_DRINK_WATER
            )
            alarmScheduler.setupAlarmDate(
                dateTime = eyesRelaxTime, Bundle().apply {
                    putInt(AppConstant.ALARM_BUNDLE_ID, ID_EYES_RELAX)
                }, requestCode = ID_EYES_RELAX
            )
            alarmScheduler.setupAlarmDate(
                dateTime = exerciseTime, Bundle().apply {
                    putInt(AppConstant.ALARM_BUNDLE_ID, ID_EXERCISE)
                }, requestCode = ID_EXERCISE
            )
            Result.success()
        } catch (e: Exception) {
            Log.d("SetUpWorker", "Error setting up alarms: ${e.message}")
            Result.failure()
        }
    }

    companion object {
        val worker = OneTimeWorkRequest.Builder(SetUpEveryDayWorker::class.java).setInitialDelay(
            2,
            TimeUnit.HOURS
        ).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build()
    }
}