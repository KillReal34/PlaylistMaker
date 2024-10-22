package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.repository.LibraryRepository

class DeleteTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): DeleteTrackLibraryInteractor {
    override suspend fun invoke(track: Track) = libraryRepository.deleteTrackLibrary(track)
}