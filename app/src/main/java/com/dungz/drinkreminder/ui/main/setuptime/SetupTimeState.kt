package com.dungz.drinkreminder.ui.main.setuptime

data class SetupTimeState(
    val startTime: String,
    val endTime: String,
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
            startTime = "06:00",
            endTime = "09:00",
        )
    }
}
