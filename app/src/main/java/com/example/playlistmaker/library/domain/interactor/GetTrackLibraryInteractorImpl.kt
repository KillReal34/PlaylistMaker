package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrackLibraryInteractorImpl(
    private val libraryRepository: LibraryRepository): GetTrackLibraryInteractor {
    override fun invoke(): Flow<List<Track>> = libraryRepository.getTrackLibrary().map { tracks -> tracks.reversed() }
}