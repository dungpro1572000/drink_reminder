package com.dungz.drinkreminder.utilities

import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.di.IoDispatcher
import javax.inject.Inject

class TimeController @Inject constructor(
    private val appDatabase: AppDatabase,
    @IoDispatcher
    private val ioDispatcher: IoDispatcher
) {

}