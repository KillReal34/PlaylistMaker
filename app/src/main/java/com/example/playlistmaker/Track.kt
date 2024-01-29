package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
@Parcelize
data class Track(
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
) : Parcelable {
    val coverArtWork: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
