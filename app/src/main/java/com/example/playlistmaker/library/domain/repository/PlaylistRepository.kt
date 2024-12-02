package com.example.playlistmaker.library.domain.repository

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    suspend fun getPlaylist(): Flow<List<Playlist>>
}