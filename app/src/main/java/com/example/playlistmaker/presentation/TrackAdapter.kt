package com.example.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track


class TrackAdapter(private val trackListLiveData: LiveData<List<Track>>, val onClickListener: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private const val DEBOUNCE_DELAY = 1000L
    }

    private var clickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

//    val onClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackListLiveData.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackListLiveData.value?.getOrNull(position) ?: return
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onClickListener(track)
        }
    }

    fun clickDebounce(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, DEBOUNCE_DELAY)
        }
        return current
    }
}