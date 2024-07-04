package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface SearchTracksByNameInteractor {
    suspend operator fun invoke(namePattern: String): List<Track>
}
