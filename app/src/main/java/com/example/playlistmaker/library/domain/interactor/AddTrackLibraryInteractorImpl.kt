package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.repository.LibraryRepository

class AddTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): AddTrackLibraryInteractor {
    override suspend fun invoke(track: Track) = libraryRepository.addTrackLibrary(track)
}