package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }

    override fun deleteTrackById(trackId: Int) {
        return repository.deleteTrackById(trackId)
    }

    override fun getPlaylistBId(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }
}