package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.domain.entities.Track

class SearchTracksByNameInteractorImpl(
    private val trackRepository: TrackRepository
) : SearchTracksByNameInteractor {
    override fun invoke(
        namePattern: String,
        onSuccess: (trackList: List<Track>) -> Unit,
        onFailure: (exception: Throwable) -> Unit
    ) = trackRepository.searchByName(
        pattern = namePattern,
        onSuccess = onSuccess,
        onFailure = onFailure,
    )
}
