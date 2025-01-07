package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistDao
import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistEntity
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import com.example.playlistmaker.player.data.dao.TrackPlaylistDao

@Database(version = 5, entities = [TrackEntity::class, PlaylistEntity::class, TrackPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackPlaylistDao(): TrackPlaylistDao
}