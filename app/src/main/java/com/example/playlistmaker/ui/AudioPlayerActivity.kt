package com.example.playlistmaker.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.MediaPlayerClient
import com.example.playlistmaker.data.mediaplayer.MediaPlayerClientImpl
import com.example.playlistmaker.domain.api.Executor
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity(R.layout.activity_audioplayer), Executor{

    companion object {
        private const val DELAY = 500L
    }

    private var url: String = ""

    private var handler: Handler? = null

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

    private val mediaPlayerClient: MediaPlayerClient = MediaPlayerClientImpl(this)

    override fun execute(message: String) {
        btnPlay = findViewById(R.id.ib_play)
        when(message){
            "DoButtonEnable" -> btnPlay?.isEnabled = true
            "ChangeButtonDefault" -> btnPlay?.setImageResource(R.drawable.button_play)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        backButton = findViewById(R.id.button_back)
        trackImage = findViewById(R.id.iv_track_image)
        trackName = findViewById(R.id.tv_track_name)
        groupName = findViewById(R.id.tv_name_group)
        trackTime = findViewById(R.id.tv_right_time_track)
        albumName = findViewById(R.id.tv_right_album_name)
        trackYear = findViewById(R.id.tv_right_year_track)
        trackGenre = findViewById(R.id.tv_right_track_genre)
        trackCountry = findViewById(R.id.tv_right_track_country)
        btnPlay = findViewById(R.id.ib_play)
        tvPlayTime = findViewById(R.id.tv_play_time)
        tvPlayTime?.text = "00:00"

        intent?.extras?.getParcelable<Track>(SearchActivity.TRACK_EXTRA)?.let { track ->
            url = track.previewUrl
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

        mediaPlayerClient.preparePlayed(url)
        
        btnPlay?.setOnClickListener {
            when(mediaPlayerClient.getPlayerState()) {
                PlayerState.STATE_PLAYING -> btnPlay?.setImageResource(R.drawable.button_play)
                PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> btnPlay?.setImageResource(R.drawable.button_stop)
                else -> {}
            }
            mediaPlayerClient.playbackControl()
            handler?.post(updateTimerPlay())
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerClient.pausedPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.post(updateTimerPlay())
        mediaPlayerClient.release()
    }

    private fun updateTimerPlay() : Runnable {
        return object : Runnable {
            override fun run() {
                when(mediaPlayerClient.getPlayerState()) {
                    PlayerState.STATE_PLAYING -> {
                        tvPlayTime?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayerClient.currentPosition())
                        handler?.postDelayed(this, DELAY)
                    }
                    PlayerState.STATE_PAUSED -> {
                        handler?.removeCallbacks(this)
                    }
                    PlayerState.STATE_PREPARED -> {
                        tvPlayTime?.text = "00:00"
                        handler?.removeCallbacks(this)
                    }
                    else -> {}
                }
            }
        }
    }
}