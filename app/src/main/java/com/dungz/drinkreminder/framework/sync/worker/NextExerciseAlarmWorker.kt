package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import com.dungz.drinkreminder.utilities.getTodayTime
import com.dungz.drinkreminder.utilities.minuteBetween2Date
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class NextExerciseAlarmWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
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
        val nextExerciseTime = exerciseInfo.nextNotificationTime.convertStringTimeToHHmm().apply {
            time =
                time + exerciseInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
        }
        val afternoonEndTime = workingTime.endTime.convertStringTimeToHHmm()
        val workingDay = workingTime.repeatDay

        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        if (nextExerciseTime.before(afternoonEndTime) && workingDay.contains(today)) {
            appRepository.setExerciseInfo(
                exerciseInfo.copy(
                    nextNotificationTime = nextExerciseTime.formatToString(),
                    isChecked = false, // Reset the checked state for the next notification
                )
            )
            alarmScheduler.setupAlarmDate(nextExerciseTime, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EXERCISE)
            }, AppConstant.ID_EXERCISE)
            return Result.success()
        } else {
            val newDayTime = workingTime.startTime.convertStringTimeToHHmm().apply {
                time =
                    time + exerciseInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            appRepository.setExerciseInfo(
                exerciseInfo.copy(
                    nextNotificationTime = newDayTime.formatToString(),
                    isChecked = false, // Reset the checked state for the next notification
                )
            )

            // calculate for record how many time can do exercise
            val exerciseTimes =
                minuteBetween2Date(
                    workingTime.startTime,
                    workingTime.endTime
                ) / exerciseInfo.durationNotification -1

            appRepository.insertRecord(
                RecordCompleteEntity(
                    date = getTodayTime()
                )
            )
            appRepository.updateTotalExerciseTime(exerciseTimes, getTodayTime())

            val worker = OneTimeWorkRequest.Builder(NextExerciseAlarmWorker::class.java)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInitialDelay(4, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(appContext).enqueue(worker)

            return Result.success()
        }
        return Result.failure()
    }

    companion object {
        val worker = OneTimeWorkRequest.Builder(NextExerciseAlarmWorker::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}