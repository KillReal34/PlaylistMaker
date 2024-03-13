package com.example.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.Track

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    companion object{
        private const val DEBOUNCE_DELAY = 1000L
    }

    private var clickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    var onClickListener : ((Track) -> Unit)? = null
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            onClickListener?.invoke(tracks[position])
        }
    }

    fun clickDebounce(): Boolean {
       val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({clickAllowed = true}, DEBOUNCE_DELAY)
        }
        return current
    }
}