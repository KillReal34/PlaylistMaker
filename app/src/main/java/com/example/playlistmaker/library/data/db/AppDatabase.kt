package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}