package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.creationPlaylistWindow.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDatabaseConverter): PlaylistRepository {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { list ->
            list.map { convertor.convertorPlaylistEntityFromPlaylist(it) }
        }
    }
}