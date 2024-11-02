package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.repository.LibraryRepository

class DeleteTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): DeleteTrackLibraryInteractor {
    override suspend fun invoke(trackId: String) = libraryRepository.deleteTrackLibrary(trackId)
}