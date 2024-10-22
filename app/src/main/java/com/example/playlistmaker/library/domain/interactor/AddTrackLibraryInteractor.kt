package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface AddTrackLibraryInteractor {
    suspend operator fun invoke(track: Track)
}