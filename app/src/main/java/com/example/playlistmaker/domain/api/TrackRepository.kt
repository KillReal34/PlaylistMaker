package com.example.playlistmaker.domain.api

import com.example.playlistmaker.player.domain.model.Track

interface TrackRepository {
    fun trackSearch(expression: String) : List<Track>
}