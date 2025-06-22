package com.dungz.drinkreminder.ui.main.setuptime

data class SetupTimeState(
    val morningTimerStart: String,
    val morningTimerEnd: String,
    val afternoonTimerStart: String,
    val afternoonTimerEnd: String,
    val eyesNotificationStatus: Boolean = false,
    val eyesNotificationTime: Int = 30,
    val drinkWaterNotificationStatus: Boolean = true,
    val drinkWaterNotificationTime: Int = 40,
    val exerciseNotificationStatus: Boolean = false,
    val exerciseNotificationTime: Int = 55,
    val repeatDay: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7)
) {
    companion object {
        val Default = SetupTimeState(
            morningTimerStart = "06:00",
            morningTimerEnd = "09:00",
            afternoonTimerStart = "12:00",
            afternoonTimerEnd = "15:00",
        )
    }
}
