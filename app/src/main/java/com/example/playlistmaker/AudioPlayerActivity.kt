package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity(R.layout.activity_audioplayer) {

    private var backButton: ImageButton? = null
    private var trackImage: ImageView? = null
    private var trackName: TextView? = null
    private var groupName: TextView? = null
    private var btnAddPlaylist: ImageButton? = null
    private var btnPlay: ImageButton? = null
    private var btnLike: ImageButton? = null
    private var tvPlayTime: TextView? = null
    private var trackTime: TextView? = null
    private var albumName: TextView? = null
    private var trackYear: TextView? = null
    private var trackGenre: TextView? = null
    private var trackCountry: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backButton = findViewById(R.id.button_back)
        trackImage = findViewById(R.id.iv_track_image)
        trackName = findViewById(R.id.tv_track_name)
        groupName = findViewById(R.id.tv_name_group)
        trackTime = findViewById(R.id.tv_right_time_track)
        albumName = findViewById(R.id.tv_right_album_name)
        trackYear = findViewById(R.id.tv_right_year_track)
        trackGenre = findViewById(R.id.tv_right_track_genre)
        trackCountry = findViewById(R.id.tv_right_track_country)

        intent?.extras?.getParcelable<Track>(SearchActivity.TRACK_EXTRA)?.let { track ->
            trackName?.text = track.trackName
            groupName?.text = track.artistName
            trackTime?.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            albumName?.text = track.collectionName
            val date = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            ).parse(track.releaseDate)
            trackYear?.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            trackGenre?.text = track.primaryGenreName
            trackCountry?.text = track.country

            val density = resources.displayMetrics.density
            val roundedCornersImage = 8 * density
            Glide.with(applicationContext)
                .load(track.coverArtWork)
                .placeholder(R.drawable.no_load_image)
                .transform(RoundedCorners(roundedCornersImage.toInt()))
                .into(trackImage ?: return@let)
        }

        backButton?.setOnClickListener {
            onBackPressed()
        }
    }
}