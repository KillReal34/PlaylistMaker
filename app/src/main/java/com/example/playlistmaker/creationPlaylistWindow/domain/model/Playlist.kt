package com.example.playlistmaker.creationPlaylistWindow.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val name: String?,
    val description: String?,
    val uri: String?,
    val listIdTracks: MutableList<Int>,
    val quentityTracks: Int,
): Parcelable {}
