package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository

class AddTrackToAuditionHistoryInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository,
): AddTrackToAuditionHistoryInteractor {
    override fun invoke(track: Track) = auditionHistoryRepository.add(track)
}