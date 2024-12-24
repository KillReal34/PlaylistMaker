package com.example.playlistmaker.library.domain.repository

import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    suspend fun getPlaylist(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    fun getTrackFormPlaylist(playlistIdList: List<Int>): Flow<List<Track>>

    fun deleteTrackById(trackId: Int)

    fun deletePlalistById(playlistId: Long)
}