package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist

class BottomSheetAdapter(private val clickListener: PlaylistClickListener)
    : RecyclerView.Adapter<BottomSheetViewHolder>() {
    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet, parent, false)
        return BottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { clickListener.playlistClick(playlist)}
    }
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
    fun interface PlaylistClickListener {
        fun playlistClick(playlist: Playlist)
    }
}
