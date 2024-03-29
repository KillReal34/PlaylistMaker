package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface SearchTracksByNameInteractor {
    operator fun invoke(namePattern: String): Result<List<Track>>
}