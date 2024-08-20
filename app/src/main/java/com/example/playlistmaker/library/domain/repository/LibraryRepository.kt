package com.example.playlistmaker.library.domain.repository

import com.example.playlistmaker.player.ui.PlayerTrack
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    suspend fun addTrackLibrary(track: PlayerTrack)

    suspend fun deleteTrackLibrary(track: PlayerTrack)

    fun getTrackLibrary(): Flow<List<PlayerTrack>>
}