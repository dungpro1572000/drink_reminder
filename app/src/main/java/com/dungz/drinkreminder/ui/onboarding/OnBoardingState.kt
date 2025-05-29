package com.dungz.drinkreminder.ui.onboarding

import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import com.dungz.drinkreminder.R

data class OnBoardingState(
    val currentPage: Int = 0,
    @StringRes val title: Int,
    @StringRes val content: Int
) {
    companion object {
        val defaultList = listOf<OnBoardingState>(
            OnBoardingState(0, R.string.onboarding_title_1, R.string.onboarding_content_1),
            OnBoardingState(1, R.string.onboarding_title_2, R.string.onboarding_content_2),
            OnBoardingState(2, R.string.onboarding_title_3, R.string.onboarding_content_3)
        )
    }
}
