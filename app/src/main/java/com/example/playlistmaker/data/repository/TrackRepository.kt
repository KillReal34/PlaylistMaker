package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.entities.Track

interface TrackRepository {
    fun searchByName(pattern: String): Result<List<Track>>
}