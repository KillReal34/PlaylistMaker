package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun addPlaylist(playlist: Playlist): Flow<Long>

    fun updatePlaylist(playlist: Playlist): Flow<Int>
}