package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class SelectedPlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    SelectedPlaylistInteractor {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override fun getTracksFromPlaylist(playlistIdList: List<Int>): Flow<List<Track>> {
        return playlistRepository.getTrackFormPlaylist(playlistIdList)
    }

    override fun deletePlaylistById(playlistId: Long) {
        return playlistRepository.deletePlalistById(playlistId)
    }
}