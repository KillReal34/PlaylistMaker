package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface TrackRepository {
    fun searchByName(
        pattern: String,
        onSuccess: (trackList: List<Track>) -> Unit,
        onFailure: (exception: Throwable) -> Unit,
    )
}
