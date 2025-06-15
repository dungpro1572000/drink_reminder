package com.dungz.drinkreminder.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {

    @Insert
    suspend fun insertDrinkWater(drinkWater: DrinkWaterEntity)

    @Query("select * from DrinkWater order by id limit 1")
    fun getDrinkInfo(): Flow<DrinkWaterEntity?>
}