package com.dungz.drinkreminder.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.getTodayTime
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val workingTime = appRepository.getWorkingTime()
    private val drinkWaterFlow = appRepository.getDrinkWaterInfo()
    private val eyesRelaxFlow = appRepository.getEyesInfo()
    private val exerciseFlow = appRepository.getExerciseInfo()
    private val tickerFlow = flow {
        while (true) {
            emit(Unit)
            delay(1000)
        }
    }

    val homeScreenState: StateFlow<HomeScreenState> = combine(
        tickerFlow,
        drinkWaterFlow,
        eyesRelaxFlow,
        exerciseFlow,
        workingTime,
    ) { _, drink, eyes, exercise, workingTime ->
        val drinkTimeLeft =
            if (drink?.nextNotificationTime == null || workingTime == null) null else
                calcTimeLeft(
                    drink.nextNotificationTime,
                    workingTime.endTime,
                    drink.durationNotification
                ).toInt()
        val eyesRelaxTimeLeft =
            if (eyes?.nextNotificationTime == null || workingTime == null) null else
                calcTimeLeft(
                    eyes.nextNotificationTime,
                    workingTime.endTime,
                    eyes.durationNotification
                ).toInt()
        val exerciseTimeLeft =
            if (exercise?.nextNotificationTime == null || workingTime == null) null else
                calcTimeLeft(
                    exercise.nextNotificationTime,
                    workingTime.endTime,
                    exercise.durationNotification
                ).toInt()

        val isCheckedDrink =
            drinkTimeLeft != null && drink != null && drinkTimeLeft >= drink.durationNotification * 60 || drink?.isChecked ?: true
        val isCheckedEyes =
            eyesRelaxTimeLeft != null && eyes != null && eyesRelaxTimeLeft >= eyes.durationNotification * 60 || eyes?.isChecked ?: true
        val isCheckedExercise =
            exerciseTimeLeft != null && exercise != null && exerciseTimeLeft >= exercise.durationNotification * 60 || exercise?.isChecked ?: true
        Log.d(
            "DungNT354",
            "is drink Checked check: ${drinkTimeLeft} ${drink?.durationNotification}  $isCheckedDrink && ${drink?.isChecked}"
        )
        Log.d(
            "DungNT354",
            "is exervise Checked check: ${exerciseTimeLeft} ${eyes?.durationNotification}  $isCheckedExercise && ${eyes?.isChecked}"
        )
        Log.d(
            "DungNT354",
            "is eyes Checked check: ${eyesRelaxTimeLeft} ${exercise?.durationNotification}  $isCheckedEyes && ${exercise?.isChecked}"
        )

        HomeScreenState(
            drinkTimeLeft = drinkTimeLeft,
            eyesRelaxTimeLeft = eyesRelaxTimeLeft,
            exerciseTimeLeft = exerciseTimeLeft,
            drinkTime = drink?.nextNotificationTime,
            eyesRelaxTime = eyes?.nextNotificationTime,
            exerciseTime = exercise?.nextNotificationTime,
            isCheckedDrink = isCheckedDrink,
            isCheckedEyes = isCheckedEyes,
            isCheckedExercise = isCheckedExercise
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        HomeScreenState.Default
    )

    fun setUpTime() {
        viewModelScope.launch {
            alarmScheduler.setupAlarmTimeMillis(5000L, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
            }, AppConstant.ID_EYES_RELAX)
        }
    }

    fun saveRecordDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateDrinkChecked(true)
            appRepository.updateRecordDrinkTime(getTodayTime())
        }
    }

    fun saveRecordEyesRelax() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateEyesChecked(true)
            appRepository.updateRecordEyesRelaxTime(getTodayTime())
        }
    }

    fun saveRecordExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateExerciseChecked(true)
            appRepository.updateRecordExerciseTime(getTodayTime())
        }
    }

    fun setUpAlarm() {
        alarmScheduler.setupAlarmTimeMillis(5000L, Bundle().apply {
            putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
        }, AppConstant.ID_EYES_RELAX)
    }
}

fun calcTimeLeft(timeString: String?, lastWorkingTime: String, nextTimeDuration: Int): Long {
    return try {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val localTime = LocalTime.parse(timeString ?: "", formatter)
        val lasWorkingTime1 = LocalTime.parse(lastWorkingTime, formatter)


        val todayTarget = now.toLocalDate().atTime(localTime)
        val lastWorking = now.toLocalDate().atTime(lasWorkingTime1)

        val finalTarget = if (todayTarget.isBefore(now)) {
            if (lastWorking.isBefore(now)) {
                todayTarget.plusDays(1)
            } else {
                todayTarget.plusMinutes(nextTimeDuration.toLong())
            }
        } else {
            todayTarget
        }
        Log.d("DungNT354444", " calcTimeLeft: $finalTarget")
        Duration.between(now, finalTarget).seconds
    } catch (e: Exception) {
        Log.d("DungNT354444", " calcTimeLeft: ${e.message}")
        0L
    }
}
