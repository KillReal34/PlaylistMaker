package com.example.playlistmaker.library.domain.repository

import com.example.playlistmaker.library.data.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long>
    fun updatePlaylist(playlist: PlaylistEntity): Flow<Int>
}