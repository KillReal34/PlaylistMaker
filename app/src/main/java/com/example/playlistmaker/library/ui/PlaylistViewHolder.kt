package com.example.playlistmaker.library.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var id: Long? = null
    private val cover: ImageView = itemView.findViewById(R.id.iv_playlist_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name_item)
    private val playlistQuantity: TextView = itemView.findViewById(R.id.tv_playlist_quantity_item)

    fun bind(playlist: Playlist) {
        id = playlist.id

        Glide.with(itemView)
            .load(playlist.uri)
            .placeholder(R.drawable.no_load_image_512x512)
            .transform(CenterCrop(), RoundedCorners(8))
            .into(cover)
        id = playlist.id
        playlistName.text = playlist.name
        playlistQuantity.text = rightEnding(playlist.listIdTracks.size)
    }

    private fun rightEnding(quantityTracks: Int): String{
        val ending = when (quantityTracks % 10) {
            1 -> " трек"
            2, 3, 4, -> " трека"
            else -> " треков"
        }
        return quantityTracks.toString() + ending
    }
}