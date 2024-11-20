package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase
): PlaylistRepository {
    override fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long> = flow {
        val plailist = appDatabase.playlistDao().insertPlaylist(playlist)
        emit(plailist)
    }

    override fun updatePlaylist(playlist: PlaylistEntity): Flow<Int> = flow {
        val updater = appDatabase.playlistDao().updatePlaylist(playlist)
        emit(updater)
    }

}