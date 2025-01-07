package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylist(): Flow<List<Playlist>>
    fun deleteTrackById(trackId: Int)
    fun getPlaylistBId(playlistId: Long): Flow<Playlist>
}