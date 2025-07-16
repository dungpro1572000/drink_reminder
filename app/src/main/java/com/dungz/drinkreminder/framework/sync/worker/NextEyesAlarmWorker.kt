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
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import com.dungz.drinkreminder.utilities.minuteBetween2Date
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class NextEyesAlarmWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val eyesRelaxInfo = appRepository.getEyesInfo().firstOrNull()
        val workingTime = appRepository.getWorkingTime().firstOrNull()
        if (eyesRelaxInfo == null || workingTime == null) {
            return Result.failure()
        }
        val nextEyesRelaxTime = eyesRelaxInfo.nextNotificationTime.convertStringTimeToHHmm().apply {
            time =
                time + eyesRelaxInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
        }
        val afternoonEndTime = workingTime.afternoonEndTime.convertStringTimeToHHmm()
        val workingDay = workingTime.repeatDay

        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        if (nextEyesRelaxTime.before(afternoonEndTime) && workingDay.contains(today)) {
            appRepository.setEyeInfo(
                eyesRelaxInfo.copy(
                    nextNotificationTime = nextEyesRelaxTime.formatToString(),
                    isChecked = false // Reset the checked state for the next notification
                )
            )
            alarmScheduler.setupAlarmDate(nextEyesRelaxTime, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
            }, AppConstant.ID_EYES_RELAX)
        } else {

            // set workManager for next day
            val newDayTime = workingTime.morningStartTime.convertStringTimeToHHmm().apply {
                time =
                    time + eyesRelaxInfo.durationNotification * 60 * 1000 // Convert minutes to milliseconds
            }
            appRepository.setEyeInfo(
                eyesRelaxInfo.copy(
                    nextNotificationTime = newDayTime.formatToString(),
                    isChecked = false // Reset the checked state for the next notification
                )
            )
            // calculate for record how many time can do exercise
            val exerciseTimes =
                (minuteBetween2Date(workingTime.morningStartTime, workingTime.morningEndTime) +
                        minuteBetween2Date(
                            workingTime.afternoonStartTime,
                            workingTime.afternoonEndTime
                        )) / eyesRelaxInfo.durationNotification
            val worker =
                OneTimeWorkRequest.Builder(NextEyesAlarmWorker::class.java).setInitialDelay(
                    4,
                    TimeUnit.HOURS
                )
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build()
            WorkManager.getInstance(context = appContext).enqueue(worker)
        }
        return Result.success()
    }

    companion object {
        val startWorker = OneTimeWorkRequest.Builder(NextEyesAlarmWorker::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}