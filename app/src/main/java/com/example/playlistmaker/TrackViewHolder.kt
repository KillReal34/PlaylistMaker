package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackActor: TextView = itemView.findViewById(R.id.textViewNameGroup)
    private val trackTime: TextView = itemView.findViewById(R.id.textViewTimeTrack)

    fun bind(item: Track) {
        trackName.text = item.trackName
        trackActor.text = item.artistName
        trackTime.text = item.trackTime
        Glide.with(itemView)
            .load(item.artWorkUrl100)
            .centerCrop()
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.no_load_image)
            .into(trackImage)
    }
}