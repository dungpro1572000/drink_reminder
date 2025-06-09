package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkingTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkingTime(workingTime: WorkingTime)

    @Query("select * from workingtime order by id limit 1")
    fun getWorkingTime(): Flow<WorkingTime>
}