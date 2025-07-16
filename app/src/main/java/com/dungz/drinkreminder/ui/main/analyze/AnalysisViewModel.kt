package com.dungz.drinkreminder.ui.main.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.RecordCompleteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AnalysisViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _recordDataFlow = MutableStateFlow<List<RecordCompleteModel>>(emptyList())
    val recordDataFlow: StateFlow<List<RecordCompleteModel>> get() = _recordDataFlow.asStateFlow()

    fun loadData() {
        // Load data from the repository and update _recordDataFlow
        // This is a placeholder for actual data loading logic
        // Example: _recordDataFlow.value = appRepository.getRecordData()
        viewModelScope.launch {
            appRepository.getRecordComplete().collect {
                _recordDataFlow.emit(it)
            }
        }

    }
}