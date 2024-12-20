package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }
}