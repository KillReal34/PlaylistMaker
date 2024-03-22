package com.example.playlistmaker.search.data.repository

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.data.persistence.AuditionHistoryPersistence
import com.example.playlistmaker.search.domain.entities.AuditionHistory

class AuditionHistoryRepositoryImpl(
    private val auditionHistoryPersistence: AuditionHistoryPersistence
): AuditionHistoryRepository {
    override fun add(track: Track) = auditionHistoryPersistence.add(track = track)

    override fun clear() = auditionHistoryPersistence.clear()

    override fun getLiveData(): LiveData<AuditionHistory> = auditionHistoryPersistence.getLiveData()
}