package com.example.dogfoodscanner.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["barcode"], unique = true)])
data class DogFood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String,
    val barcode: String,
    val imageUri: String,
    val caloriesKcal: Double,
    val proteinG: Double,
    val fatG: Double,
    val fiberG: Double,
    val ashG: Double,
    val moistureG: Double,
    val calciumMg: Double,
    val phosphorusMg: Double,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DogFood::class,
            parentColumns = ["id"],
            childColumns = ["dogFoodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dogFoodId")]
)
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dogFoodId: Long,
    val amountG: Double,
    val timestamp: Long,
)

@Entity(indices = [Index(value = ["date"], unique = true)])
data class DailyTarget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val targetCaloriesKcal: Double,
    val targetProteinG: Double,
    val targetFatG: Double,
    val targetFiberG: Double,
)
