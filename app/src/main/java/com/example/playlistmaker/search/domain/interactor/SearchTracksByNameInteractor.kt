package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface SearchTracksByNameInteractor {
    operator fun invoke(
        namePattern: String,
        onSuccess: (trackList: List<Track>) -> Unit,
        onFailure: (exception: Throwable) -> Unit,
    )
}
