package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TrackAdapter(
    private val onTrackClick: (track: Track) -> Unit,
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private val DEBOUNCE_DELAY = 1.seconds
    }

    private var clickAllowed = true

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
            GlobalScope.launch {
                clickAllowed = true
                delay(DEBOUNCE_DELAY.inWholeMilliseconds)
            }
        }
        return current
    }
}
