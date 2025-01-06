package com.example.playlistmaker.creationPlaylistWindow.domain.repository

import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {
    fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long>
    fun updatePlaylist(playlist: PlaylistEntity): Flow<Int>
    fun savePlaylist(uri: String): String
}