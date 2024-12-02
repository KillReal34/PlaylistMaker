package com.example.playlistmaker.library.domain.interactor

interface DeleteTrackLibraryInteractor {
    suspend operator fun invoke(trackId: String)
}