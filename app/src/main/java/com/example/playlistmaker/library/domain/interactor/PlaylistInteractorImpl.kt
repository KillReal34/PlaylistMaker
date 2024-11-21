package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistDatabaseConverter: PlaylistDatabaseConverter,
): PlaylistInteractor {
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