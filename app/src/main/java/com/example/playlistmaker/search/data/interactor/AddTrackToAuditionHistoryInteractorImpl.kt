package com.example.playlistmaker.search.data.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.data.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor

class AddTrackToAuditionHistoryInteractorImpl (
    private val auditionHistoryRepository: AuditionHistoryRepository,
): AddTrackToAuditionHistoryInteractor {
    override fun invoke(track: Track) = auditionHistoryRepository.add(track)
}