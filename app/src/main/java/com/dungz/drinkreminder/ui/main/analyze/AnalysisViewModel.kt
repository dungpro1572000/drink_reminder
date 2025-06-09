package com.dungz.drinkreminder.ui.main.analyze

import androidx.lifecycle.ViewModel
import com.dungz.drinkreminder.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(appRepository: AppRepository): ViewModel() {
}