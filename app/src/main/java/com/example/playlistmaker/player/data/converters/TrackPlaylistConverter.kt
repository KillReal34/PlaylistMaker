package com.example.playlistmaker.player.data.converters

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.data.TrackPlaylistEntity

class TrackPlaylistConverter {
    fun converterTrackFromTrackPlaylistEntity(track: Track): TrackPlaylistEntity {
        return TrackPlaylistEntity(
            trackId = track.trackId,
            artworkUrl100 = track.artworkUrl,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.duration.inWholeMilliseconds,
            previewUrl = track.previewUrl,
        )
    }
}