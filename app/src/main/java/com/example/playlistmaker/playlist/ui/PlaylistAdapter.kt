package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.ui.TrackViewHolder

class PlaylistAdapter(
    private val onClickListener: (track: Track) -> Unit,
    private val onLongClickListener:  (track: Track) -> Boolean,): RecyclerView.Adapter<TrackViewHolder>() {

    var tracks: MutableList<Track> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onClickListener(track) }
        holder.itemView.setOnLongClickListener {onLongClickListener(track)}
    }
}