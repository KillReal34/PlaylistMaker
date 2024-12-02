package com.example.playlistmaker.player.ui

import android.os.Parcelable
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.ui.coverArtWork
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerTrack(
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackName: String,
    val artistName: String,
    val duration: Long,
    val coverArtWork: String,
    val previewUrl: String,
    var isFavorite: Boolean,
) : Parcelable {
    constructor(track: Track) : this(
        trackId = track.trackId,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        trackName = track.trackName,
        artistName = track.artistName,
        duration = track.duration.inWholeMilliseconds,
        coverArtWork = track.coverArtWork,
        previewUrl = track.previewUrl,
        isFavorite = track.isFavorite,
    )
}
