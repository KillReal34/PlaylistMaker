package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override fun addPlaylist(playlist: Playlist): Flow<Long> {
        return playlistRepository.addNewPlaylist(converterPlaylistFromPlaylistEntity(playlist))
    }

    override fun updatePlaylist(playlist: Playlist): Flow<Int> {
        return playlistRepository.updatePlaylist(converterPlaylistFromPlaylistEntity(playlist))
    }

    fun converterPlaylistFromPlaylistEntity(playlist: Playlist) : PlaylistEntity{
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            uri = playlist.uri,
            listIdTracks = playlist.listIdTracks,
            quentityTracks = playlist.quentityTracks)
    }
}