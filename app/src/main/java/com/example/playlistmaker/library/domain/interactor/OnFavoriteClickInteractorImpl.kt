package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.repository.LibraryRepository

class OnFavoriteClickInteractorImpl(
    private val libraryRepository: LibraryRepository
): OnFavoriteClickInteractor {
    override suspend fun invoke(track: Track) {
        return libraryRepository.onFavoriteClicked(track = track)
    }
}