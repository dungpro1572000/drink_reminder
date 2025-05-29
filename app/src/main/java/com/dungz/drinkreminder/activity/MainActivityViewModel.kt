package com.dungz.drinkreminder.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.datastore.DataStoreManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreManagement: DataStoreManagement,
) : ViewModel() {

    val isInApp: StateFlow<Boolean> = dataStoreManagement.isFirstInAppFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly, initialValue = true
    )

    fun saveIsFirstInApp(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManagement.saveFirstInApp(boolean)
        }
    }
}