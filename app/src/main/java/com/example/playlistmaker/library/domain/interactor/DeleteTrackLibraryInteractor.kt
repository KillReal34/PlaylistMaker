package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface DeleteTrackLibraryInteractor {
    suspend operator fun invoke(track: Track)
}