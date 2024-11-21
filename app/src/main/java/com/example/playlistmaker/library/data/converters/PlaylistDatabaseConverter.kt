package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.model.Playlist
import com.google.gson.Gson

class PlaylistDatabaseConverter(private val gson: Gson) {
    fun converterPlaylistFromPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            uri = playlist.uri,
            listIdTracks = gson.toJson(playlist.listIdTracks),
            quentityTracks = playlist.quentityTracks,
        )
    }
}