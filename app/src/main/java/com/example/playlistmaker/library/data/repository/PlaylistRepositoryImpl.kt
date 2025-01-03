package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.creationPlaylistWindow.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.repository.PlaylistRepository
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import com.example.playlistmaker.player.data.converters.TrackPlaylistConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDatabaseConverter,
    private val trackPlaylistConverter: TrackPlaylistConverter): PlaylistRepository {
    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map { list ->
            list.map { convertor.convertorPlaylistEntityFromPlaylist(it) }
        }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        emit(convertor.convertorPlaylistEntityFromPlaylist(playlist))
    }

    override fun getTrackFromPlaylist(playlistIdList: List<Int>): Flow<List<Track>> {
        return appDatabase.playlistDao().getAllTracksFromPlaylist().map { listEntity ->
            trackFilter(listEntity, playlistIdList)
        }
    }

    override fun deleteTrackById(trackId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.playlistDao().deleteTrackById(trackId)
        }
    }

    override fun deletePlaylistById(playlistId: Long) {
        TODO("Not yet implemented")
    }

    private fun trackFilter(
        playlistEntity: List<TrackPlaylistEntity>,
        playlistIdList: List<Int>
    ): List<Track> {
        val result = mutableListOf<Track>()
        for (track: TrackPlaylistEntity in playlistEntity)
            if (playlistIdList.any { it == track.trackId.toInt() })
            result.add(trackPlaylistConverter.converterTrackPlaylistEntityFromTrack(track))
        return result
    }
}