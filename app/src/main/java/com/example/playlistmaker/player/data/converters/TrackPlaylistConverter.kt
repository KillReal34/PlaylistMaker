package com.example.playlistmaker.player.data.converters

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import kotlin.time.Duration.Companion.milliseconds

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
            System.currentTimeMillis(),
        )
    }

    fun converterTrackPlaylistEntityFromTrack(trackPlaylistEntity: TrackPlaylistEntity): Track {
        return Track(
            trackId = trackPlaylistEntity.trackId,
            artworkUrl = trackPlaylistEntity.artworkUrl100,
            collectionName = trackPlaylistEntity.collectionName,
            releaseDate = trackPlaylistEntity.releaseDate,
            primaryGenreName = trackPlaylistEntity.primaryGenreName,
            country = trackPlaylistEntity.country,
            trackName = trackPlaylistEntity.trackName,
            artistName = trackPlaylistEntity.artistName,
            duration = trackPlaylistEntity.trackTimeMillis.milliseconds,
            previewUrl = trackPlaylistEntity.previewUrl,
        )
    }
}