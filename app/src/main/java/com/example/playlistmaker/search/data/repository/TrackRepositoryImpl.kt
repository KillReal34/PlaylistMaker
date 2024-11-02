package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.domain.repository.TrackRepository
import kotlin.time.Duration.Companion.milliseconds

class TrackRepositoryImpl(
    private val iTunesApi: ITunesApi,
    private val appDatabase: AppDatabase,
) : TrackRepository {
    override suspend fun searchByName(pattern: String): List<Track> {
        val searchedTracks = iTunesApi.search(pattern)
        .results
        .map { trackDto ->
            Track(
                trackId = trackDto.trackId.toString(),
                collectionName = trackDto.collectionName,
                releaseDate = trackDto.releaseDate,
                primaryGenreName = trackDto.primaryGenreName,
                country = trackDto.country,
                trackName = trackDto.trackName,
                artistName = trackDto.artistName,
                duration = trackDto.trackTimeMillis.milliseconds,
                artworkUrl = trackDto.artworkUrl100,
                previewUrl = trackDto.previewUrl,
            )
        }
        val trackIdList = appDatabase.trackDao().getTracksIdList()

        searchedTracks.forEach { track ->
            track.isFavorite = track.trackId in trackIdList
        }

        return searchedTracks
    }
}
