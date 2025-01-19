package com.example.playlistmaker.creationPlaylistWindow.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistInteractor {
    fun addPlaylist(playlist: Playlist): Flow<Long>

    fun addTrackInPlaylist(track: Track)

    fun updatePlaylist(playlist: Playlist): Flow<Int>

    fun savePlaylist(uri: String): String

    fun deletePlaylistById(playlistId: Long): Flow<Long>
}