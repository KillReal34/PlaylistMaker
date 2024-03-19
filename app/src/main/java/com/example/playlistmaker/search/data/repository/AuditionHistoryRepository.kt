package com.example.playlistmaker.search.data.repository

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.entities.AuditionHistory

interface AuditionHistoryRepository {
    fun add(track: Track)

    fun clear()

    fun getLiveData(): LiveData<AuditionHistory>
}