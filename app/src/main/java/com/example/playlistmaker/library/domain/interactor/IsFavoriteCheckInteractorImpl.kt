package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.repository.LibraryRepository

class IsFavoriteCheckInteractorImpl(
    private val libraryRepository: LibraryRepository
): IsFavoriteCheckInteractor {
    override suspend fun invoke(trackId: String): Boolean {
        return libraryRepository.isFavorite(trackId)
    }

}