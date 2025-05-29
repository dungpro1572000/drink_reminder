package com.dungz.drinkreminder.data.datastore

import android.content.Context
import android.preference.Preference
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreManagement @Inject constructor(@ApplicationContext val context: Context) {
    private val isFirstInApp = booleanPreferencesKey("is_linear_layout_manager")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = FIRST_IN_APP
    )

    suspend fun saveFirstInApp(isFirstInApp: Boolean) {
        context.dataStore.edit { preference ->
            preference[this@DataStoreManagement.isFirstInApp] = isFirstInApp
        }
    }

    val isFirstInAppFlow: Flow<Boolean> = context.dataStore.data.map { preference ->
        preference[isFirstInApp] ?: true
    }

    companion object {
        const val FIRST_IN_APP = "First_In_App_Name"

    }
}