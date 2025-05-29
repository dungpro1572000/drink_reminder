package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.model.Eyes
import kotlinx.coroutines.flow.Flow

@Dao
interface EyesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eyes: Eyes)

    @Query("Select * from Eyes")
    fun getAllEyes(): Flow<List<Eyes>>

    @Query("Select * from Eyes where isNoticed = 1 order by id desc limit 1")
    suspend fun getLastNoticed(): Eyes?

    @Query("Delete from Eyes")
    suspend fun delete()
}