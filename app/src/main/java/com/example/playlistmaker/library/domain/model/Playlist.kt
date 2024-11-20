package com.example.playlistmaker.library.domain.model

import android.os.Parcelable
import com.example.playlistmaker.library.data.db.PlaylistEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val uri: String,
    val listIdTracks: String,
    val quentityTracks: Int,
): Parcelable {
    constructor(playlistEntity: PlaylistEntity) : this(
        id = playlistEntity.id,
        name = playlistEntity.name,
        description = playlistEntity.description,
        uri = playlistEntity.uri,
        listIdTracks = playlistEntity.listIdTracks,
        quentityTracks = playlistEntity.quentityTracks,
    )
}
