package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.repository.LibraryRepository
import com.example.playlistmaker.player.ui.PlayerTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): GetTrackLibraryInteractor {
    override fun invoke(): Flow<List<PlayerTrack>> = libraryRepository.getTrackLibrary().map { tracks -> tracks.reversed() }
}