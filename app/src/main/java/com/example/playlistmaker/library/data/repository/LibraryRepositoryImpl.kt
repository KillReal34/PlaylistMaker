package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds

class LibraryRepositoryImpl(
    private val appDatabase: AppDatabase
) : LibraryRepository {
    override suspend fun addTrackLibrary(track: Track) {
        appDatabase.trackDao().insertTrack(converterTrackFromTrackEntity(track))
    }

    override suspend fun deleteTrackLibrary(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    override fun getTrackLibrary(): Flow<List<Track>> {
        val tracks = appDatabase.trackDao().getTracks()
        return tracks.map { tracks ->
            converterTracksFromTrackEntity(tracks)
        }
    }

    private fun converterTracksFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            Track(
                trackId = trackEntity.trackId,
                trackName = trackEntity.trackName,
                collectionName = trackEntity.collectionName,
                releaseDate = trackEntity.releaseDate,
                primaryGenreName = trackEntity.primaryGenreName,
                country = trackEntity.country,
                artistName = trackEntity.artistName,
                duration = trackEntity.duration.milliseconds,
                artworkUrl = trackEntity.artworkUrl,
                previewUrl = trackEntity.previewUrl
            )
        }
    }

    private fun converterTrackFromTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            artistName = track.artistName,
            duration = track.duration.inWholeMilliseconds,
            artworkUrl = track.artworkUrl,
            previewUrl = track.previewUrl,
            trackName = track.trackName
        )
    }
}