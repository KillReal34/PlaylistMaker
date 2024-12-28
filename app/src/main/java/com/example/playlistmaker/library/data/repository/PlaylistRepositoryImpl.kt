package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.creationPlaylistWindow.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDatabaseConverter): PlaylistRepository {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { list ->
            list.map { convertor.convertorPlaylistEntityFromPlaylist(it) }
        }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow{
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        emit(convertor.convertorPlaylistEntityFromPlaylist(playlist))
    }

    override fun getTrackFormPlaylist(playlistIdList: List<Int>): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override fun deleteTrackById(trackId: Int) {
        TODO("Not yet implemented")
    }

    override fun deletePlaylistById(playlistId: Long) {
        TODO("Not yet implemented")
    }
}