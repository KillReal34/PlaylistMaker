package com.example.playlistmaker.search.data.interactor

import com.example.playlistmaker.search.data.repository.TrackRepository
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor

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
