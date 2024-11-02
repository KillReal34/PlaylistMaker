package com.example.playlistmaker.library.domain.repository

import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    suspend fun addTrackLibrary(track: Track)

    suspend fun deleteTrackLibrary(track: Track)

    suspend fun deleteTrackLibrary(trackId: String)

    fun getTrackLibrary(): Flow<List<Track>>
}