package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface SelectedPlaylistInteractor {
    fun getPlaylistById(playlistId: Long): Flow<Playlist>
    fun getTracksFromPlaylist(playlistIdList: List<Int>): Flow<List<Track>>
    fun deletePlaylistById(playlistId: Long)
}