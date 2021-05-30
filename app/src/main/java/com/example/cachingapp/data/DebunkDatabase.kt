package com.example.cachingapp.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Debunk::class], version = 1
)
abstract class DebunkDatabase: RoomDatabase() {

    abstract fun debunkDao(): DebunkDao
}