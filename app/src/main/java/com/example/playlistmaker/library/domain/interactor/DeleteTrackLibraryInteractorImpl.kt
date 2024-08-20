package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.repository.LibraryRepository
import com.example.playlistmaker.player.ui.PlayerTrack

class DeleteTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): DeleteTrackLibraryInteractor {
    override suspend fun invoke(track: PlayerTrack) = libraryRepository.deleteTrackLibrary(track)
}