package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseEntity: ExerciseEntity)

    @Query("Select * from Exercise order by id limit 1")
    fun getAllExercise(): Flow<ExerciseEntity?>
}