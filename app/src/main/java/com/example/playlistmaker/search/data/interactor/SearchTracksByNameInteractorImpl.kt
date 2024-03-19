package com.example.playlistmaker.search.data.interactor

import com.example.playlistmaker.data.repository.TrackRepository
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor

class SearchTracksByNameInteractorImpl(
    private val trackRepository: TrackRepository
) : SearchTracksByNameInteractor {
    override fun invoke(namePattern: String): Result<List<Track>> =
        trackRepository.searchByName(namePattern)
}