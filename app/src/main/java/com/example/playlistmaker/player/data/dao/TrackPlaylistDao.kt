package com.example.playlistmaker.player.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.player.data.TrackPlaylistEntity

@Dao
interface TrackPlaylistDao {
    @Insert(entity = TrackPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrackFromPlaylist(track: TrackPlaylistEntity)
}