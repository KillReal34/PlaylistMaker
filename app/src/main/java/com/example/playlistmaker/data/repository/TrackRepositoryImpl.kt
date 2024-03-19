package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.domain.entities.Track
import kotlin.time.Duration.Companion.milliseconds


class TrackRepositoryImpl(private val iTunesApi: ITunesApi) : TrackRepository {
    override fun searchByName(pattern: String): Result<List<Track>> = iTunesApi.search(pattern)
        .runCatching { execute().body() }
        .map { trackResponse ->
            if (trackResponse == null || trackResponse.results.isEmpty()) return@map emptyList()

            trackResponse.results.map { trackDto ->
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
        }
}