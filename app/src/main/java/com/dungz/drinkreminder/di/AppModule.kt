package com.dungz.drinkreminder.di

import android.content.Context
import androidx.room.Room
import com.dungz.drinkreminder.data.datastore.DataStoreManagement
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.repository.AppRepositoryImpl
import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.data.roomdb.dao.ExerciseDao
import com.dungz.drinkreminder.data.roomdb.dao.EyesDao
import com.dungz.drinkreminder.data.roomdb.dao.RecordCompleteDao
import com.dungz.drinkreminder.data.roomdb.dao.WorkingTimeDao
import com.dungz.drinkreminder.framework.notification.NotificationManager
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManagement {
        return DataStoreManagement(context)
    }

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEyesDao(db: AppDatabase): EyesDao = db.eyesDao()

    @Provides
    fun provideWaterDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideWorkingTimeDao(db: AppDatabase): WorkingTimeDao = db.workingTimeDao()

    @Provides
    fun provideRecordCompleteDao(db: AppDatabase): RecordCompleteDao = db.recordCompleteDao()

    @Provides
    fun provideAppRepository(db: AppDatabase): AppRepository {
        return AppRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context,
        appRepository: AppRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AlarmScheduler {
        return AlarmScheduler(
            context = context, appRepository = appRepository,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return NotificationManager(context)
    }
}