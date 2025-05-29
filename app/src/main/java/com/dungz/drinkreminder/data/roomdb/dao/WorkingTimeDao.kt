package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dungz.drinkreminder.data.roomdb.model.WorkingTime

@Dao
interface WorkingTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkingTime(workingTime: WorkingTime)
}