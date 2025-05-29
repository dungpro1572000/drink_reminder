package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.model.Exercise
import com.dungz.drinkreminder.data.roomdb.model.Eyes
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eyes: Eyes)

    @Query("Select * from Exercise")
    fun getAllEyes(): Flow<List<Exercise>>

    @Query("select * from Exercise where isNoticed = 1 order by id desc limit 1")
    suspend fun getLastNoticedExercise(): Exercise?

    @Query("Delete from Exercise")
    suspend fun delete()
}