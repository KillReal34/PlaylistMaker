package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.domain.entities.Track

interface TrackRepository {
    suspend fun searchByName(pattern: String): List<Track>
}
