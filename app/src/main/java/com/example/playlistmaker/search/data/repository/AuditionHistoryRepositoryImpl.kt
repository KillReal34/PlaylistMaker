package com.example.playlistmaker.search.data.repository

import androidx.lifecycle.asFlow
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.data.persistence.AuditionHistoryPersistence
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import kotlinx.coroutines.flow.Flow

class AuditionHistoryRepositoryImpl(
    private val auditionHistoryPersistence: AuditionHistoryPersistence
) : AuditionHistoryRepository {
    override fun add(track: Track) = auditionHistoryPersistence.add(track = track)

    override fun clear() = auditionHistoryPersistence.clear()

    override fun getFlow(): Flow<AuditionHistory> =
        auditionHistoryPersistence.getLiveData().asFlow()
}