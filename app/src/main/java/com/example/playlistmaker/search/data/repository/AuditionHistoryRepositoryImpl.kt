package com.example.playlistmaker.search.data.repository

import androidx.lifecycle.asFlow
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.persistence.AuditionHistoryPersistence
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class AuditionHistoryRepositoryImpl(
    private val auditionHistoryPersistence: AuditionHistoryPersistence,
    private val appDatabase: AppDatabase,
) : AuditionHistoryRepository {
    override fun add(track: Track) = auditionHistoryPersistence.add(track = track)

    override fun clear() = auditionHistoryPersistence.clear()

    override fun getFlow(): Flow<AuditionHistory> = flow {
        val auditionHistory = auditionHistoryPersistence.getLiveData()

        val isFavoriteTracks = appDatabase.trackDao().getTracksById()

        auditionHistory.value?.trackList?.map { track ->
            if (isFavoriteTracks.contains(track.trackId)){
                track.isFavorite = true
            }
        }
        emitAll(auditionHistory.asFlow())
    }
}