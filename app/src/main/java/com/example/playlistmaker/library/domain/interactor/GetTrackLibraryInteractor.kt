package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface GetTrackLibraryInteractor {
    operator fun invoke(): Flow<List<Track>>
}