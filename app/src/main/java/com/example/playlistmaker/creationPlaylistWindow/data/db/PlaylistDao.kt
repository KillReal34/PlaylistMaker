package com.example.playlistmaker.creationPlaylistWindow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Query("SELECT * FROM track_playlist_table")
    fun getAllTracksFromPlaylist(): Flow<List<TrackPlaylistEntity>>

    @Query("DELETE FROM track_playlist_table WHERE trackId = :trackId")
    fun deleteTrackById(trackId: Int)
}