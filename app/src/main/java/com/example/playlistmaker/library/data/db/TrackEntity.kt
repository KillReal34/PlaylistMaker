package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TRACK_TABLE)
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackName: String,
    val artistName: String,
    val duration: Long,
    val artworkUrl: String,
    val previewUrl: String,
)

const val TRACK_TABLE = "track_table"
