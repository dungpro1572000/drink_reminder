package com.dungz.drinkreminder.di

import android.content.Context
import androidx.room.Room
import com.dungz.drinkreminder.data.datastore.DataStoreManagement
import com.dungz.drinkreminder.data.roomdb.AppDatabase
import com.dungz.drinkreminder.data.roomdb.dao.ExerciseDao
import com.dungz.drinkreminder.data.roomdb.dao.EyesDao
import com.dungz.drinkreminder.data.roomdb.dao.WorkingTimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        ).build()
    }

    @Provides
    fun provideEyesDao(db: AppDatabase): EyesDao = db.eyesDao()

    @Provides
    fun provideWaterDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideWorkingTimeDao(db: AppDatabase): WorkingTimeDao = db.workingTimeDao()
}