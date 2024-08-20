package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.repository.LibraryRepository
import com.example.playlistmaker.player.ui.PlayerTrack

class AddTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): AddTrackLibraryInteractor {
    override suspend fun invoke(track: PlayerTrack) = libraryRepository.addTrackLibrary(track)
}