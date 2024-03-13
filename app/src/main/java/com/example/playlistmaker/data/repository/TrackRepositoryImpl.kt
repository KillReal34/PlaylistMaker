package com.example.playlistmaker.data.repository

import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun trackSearch(expression: String): List<Track> {
        val responce = networkClient.doRequest(TrackSearchRequest(expression))

        if (responce.resultCode == 200) {
            return (responce as TrackSearchResponse).results.map {
                Track(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                    it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl)
            }
        }
        else {
            return emptyList()
        }
    }
}