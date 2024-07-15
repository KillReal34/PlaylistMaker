package com.example.playlistmaker.domain.entities

import kotlin.time.Duration

data class Track(
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackName: String,
    val artistName: String,
    val duration: Duration,
    val artworkUrl: String,
    val previewUrl: String,
){
    var isFavorite: Boolean = false
}
