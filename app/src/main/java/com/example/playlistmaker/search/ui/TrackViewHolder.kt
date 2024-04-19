package com.example.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.iv_track_image)
    private val trackName: TextView = itemView.findViewById(R.id.tv_track_name)
    private val trackActor: TextView = itemView.findViewById(R.id.tv_name_group)
    private val trackTime: TextView = itemView.findViewById(R.id.tv_left_time_track)

    fun bind(item: Track) {
        val roundedCornersImage = 10

        trackName.text = item.trackName
        trackActor.text = item.artistName
        trackTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault(),
        ).format(item.duration.inWholeMilliseconds)
        Glide.with(itemView)
            .load(item.coverArtWork)
            .centerCrop()
            .transform(RoundedCorners(roundedCornersImage))
            .placeholder(R.drawable.no_load_image)
            .into(trackImage)
    }
}

val Track.coverArtWork: String
    get() = artworkUrl.replaceAfterLast('/', "512x512bb.jpg")
