package com.example.dogfoodscanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DogFoodDao {
    @Query("SELECT * FROM DogFood WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): DogFood?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(food: DogFood)

    @Query("SELECT * FROM DogFood ORDER BY name ASC")
    fun observeAll(): Flow<List<DogFood>>
}
