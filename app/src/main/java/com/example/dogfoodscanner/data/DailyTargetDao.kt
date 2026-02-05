package com.example.dogfoodscanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(target: DailyTarget)

    @Query("SELECT * FROM DailyTarget WHERE date = :date LIMIT 1")
    fun observeForDate(date: String): Flow<DailyTarget?>
}
