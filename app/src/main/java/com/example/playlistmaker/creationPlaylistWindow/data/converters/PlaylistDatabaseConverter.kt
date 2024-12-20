package com.example.playlistmaker.creationPlaylistWindow.data.converters

import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistEntity
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
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

    fun convertorPlaylistEntityFromPlaylist(playlistEntity: PlaylistEntity): Playlist{
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            uri = playlistEntity.uri,
            gson.fromJson(playlistEntity.listIdTracks, Array<Int>::class.java).toCollection(ArrayList()),
            quentityTracks = playlistEntity.quentityTracks,
        )
    }
}