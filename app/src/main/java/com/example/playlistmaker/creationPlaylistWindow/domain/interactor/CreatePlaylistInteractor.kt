package com.example.playlistmaker.creationPlaylistWindow.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistInteractor {
    fun addPlaylist(playlist: Playlist): Flow<Long>

    fun updatePlaylist(playlist: Playlist): Flow<Int>

    fun savePlaylist(uri: String): String
}