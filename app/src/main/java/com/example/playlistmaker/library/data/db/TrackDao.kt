package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM $TRACK_TABLE WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM $TRACK_TABLE")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM $TRACK_TABLE")
    suspend fun getTracksById(): List<String>
}