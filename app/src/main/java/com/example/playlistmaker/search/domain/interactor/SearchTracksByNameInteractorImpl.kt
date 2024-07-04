package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.repository.TrackRepository

class SearchTracksByNameInteractorImpl(
    private val trackRepository: TrackRepository
) : SearchTracksByNameInteractor {
    override suspend fun invoke(namePattern: String) =
        trackRepository.searchByName(pattern = namePattern)
}
