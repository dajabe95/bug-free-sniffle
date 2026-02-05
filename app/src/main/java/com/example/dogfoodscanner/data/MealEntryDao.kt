package com.example.dogfoodscanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MealEntry)

    @Query("SELECT * FROM MealEntry WHERE timestamp BETWEEN :start AND :end")
    fun observeEntriesBetween(start: Long, end: Long): Flow<List<MealEntry>>
}
