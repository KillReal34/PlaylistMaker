package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import kotlinx.coroutines.flow.Flow

interface AuditionHistoryRepository {
    fun add(track: Track)

    fun clear()

    suspend fun getFlow(): Flow<AuditionHistory>
}