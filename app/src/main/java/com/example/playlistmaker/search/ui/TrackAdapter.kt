package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import kotlin.time.Duration.Companion.seconds

class TrackAdapter(
    private val onTrackClick: (track: Track) -> Unit,
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private val DEBOUNCE_DELAY = 1.seconds
    }

    private var clickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    var trackList = emptyList<Track>()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track, parent, false)

        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]

        holder.apply {
            bind(item = track)
            itemView.setOnClickListener {
                if (!clickDebounce()) return@setOnClickListener

                onTrackClick(track)
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = clickAllowed

        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed(
                { clickAllowed = true },
                DEBOUNCE_DELAY.inWholeMilliseconds,
            )
        }

        return current
    }
}
