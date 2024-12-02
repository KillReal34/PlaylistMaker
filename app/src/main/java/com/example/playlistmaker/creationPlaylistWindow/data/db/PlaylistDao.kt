package com.example.playlistmaker.creationPlaylistWindow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>
}