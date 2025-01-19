package com.example.playlistmaker.creationPlaylistWindow.domain.repository

import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistEntity
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {
    fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long>
    fun addTrackInPlaylist(track: TrackPlaylistEntity)
    fun updatePlaylist(playlist: PlaylistEntity): Flow<Int>
    fun savePlaylist(uri: String): String
    fun deletePlaylistById(playlistId: Long): Flow<Long>
}