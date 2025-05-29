package com.dungz.drinkreminder.utilities

import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.di.IoDispatcher
import javax.inject.Inject

class TimeController @Inject constructor(
    appDatabase: AppDatabase,
    @IoDispatcher
    ioDispatcher: IoDispatcher
) {

}