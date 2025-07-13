package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EyesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eyes: EyesEntity)

    @Query("Select * from Eyes order by id limit 1")
    fun getAllEyes(): Flow<EyesEntity?>

    @Query("Update Eyes set isChecked = :isChecked where id = 0")
    fun updateEyesChecked(isChecked: Boolean)
}