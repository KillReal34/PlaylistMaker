package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TRACK_TABLE)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String?,
    val description: String?,
    val uri: String?,
    val listIdTracks: String?,
    val quentityTracks: Int?,
)
