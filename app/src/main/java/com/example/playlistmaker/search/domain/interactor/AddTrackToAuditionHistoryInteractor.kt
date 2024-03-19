package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface AddTrackToAuditionHistoryInteractor {
    operator fun invoke(track: Track)
}