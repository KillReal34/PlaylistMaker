package com.example.playlistmaker

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
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity(R.layout.activity_audioplayer) {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }
    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()
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

        preparePlayed()

        btnPlay?.setOnClickListener {
            playbackControl()
            handler?.post(updateTimerPlay())
        }
    }

    override fun onPause() {
        super.onPause()
        pausedPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.post(updateTimerPlay())
        mediaPlayer.release()
    }

    private fun preparePlayed() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            btnPlay?.setImageResource(R.drawable.button_play)
        }
    }

    private fun startPlay() {
        mediaPlayer.start()
        btnPlay?.setImageResource(R.drawable.button_stop)
        playerState = STATE_PLAYING
    }

    private fun pausedPlay() {
        mediaPlayer.pause()
        btnPlay?.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {pausedPlay()}
            STATE_PAUSED, STATE_PREPARED -> {startPlay()}
        }
    }

    private fun updateTimerPlay() : Runnable {
        return object : Runnable {
            override fun run() {
                when(playerState) {
                    STATE_PLAYING -> {
                        tvPlayTime?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                        handler?.postDelayed(this, DELAY)
                    }
                    STATE_PAUSED -> {
                        handler?.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        tvPlayTime?.text = "00:00"
                        handler?.removeCallbacks(this)
                    }
                }
            }
        }
    }
}