package com.example.dogfoodscanner.data

import kotlinx.coroutines.flow.Flow

class DogFoodRepository(private val dao: DogFoodDao) {
    suspend fun getByBarcode(barcode: String): DogFood? = dao.findByBarcode(barcode)

    suspend fun save(food: DogFood) = dao.upsert(food)

    fun observeAll(): Flow<List<DogFood>> = dao.observeAll()
}
