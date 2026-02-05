package com.example.dogfoodscanner.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogFood::class, MealEntry::class, DailyTarget::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogFoodDao(): DogFoodDao
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun dailyTargetDao(): DailyTargetDao
}
