package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.domain.entities.Track

interface TrackRepository {
    fun searchByName(pattern: String): List<Track>
}