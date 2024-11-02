package com.example.playlistmaker.library.ui.extensions

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.ui.PlayerTrack
import kotlin.time.Duration.Companion.milliseconds

fun PlayerTrack.toTrackEntity(): Track = Track(
    trackId = trackId,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    trackName = trackName,
    artistName = artistName,
    duration = duration.milliseconds,
    artworkUrl = coverArtWork,
    previewUrl = previewUrl,
).apply { isFavorite = this@toTrackEntity.isFavorite }