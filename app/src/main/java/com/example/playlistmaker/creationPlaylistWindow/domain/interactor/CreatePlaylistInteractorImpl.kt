package com.example.playlistmaker.creationPlaylistWindow.domain.interactor

import com.example.playlistmaker.creationPlaylistWindow.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.creationPlaylistWindow.domain.repository.CreatePlaylistRepository
import kotlinx.coroutines.flow.Flow

class CreatePlaylistInteractorImpl(
    private val playlistRepository: CreatePlaylistRepository,
    private val playlistDatabaseConverter: PlaylistDatabaseConverter,
): CreatePlaylistInteractor {
    override fun addPlaylist(playlist: Playlist): Flow<Long> {
        return playlistRepository.addNewPlaylist(playlistDatabaseConverter.converterPlaylistFromPlaylistEntity(playlist))
    }

    override fun updatePlaylist(playlist: Playlist): Flow<Int> {
        return playlistRepository.updatePlaylist(playlistDatabaseConverter.converterPlaylistFromPlaylistEntity(playlist))
    }

    override fun savePlaylist(uri: String): String {
        return playlistRepository.savePlaylist(uri)
    }
}