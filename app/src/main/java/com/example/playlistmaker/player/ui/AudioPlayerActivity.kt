package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.api.Executor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.ui.SearchActivity
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity(), Executor {

    companion object {
        private const val DELAY = 500L
    }

    private var url: String = ""

    private var handler: Handler? = null

    private lateinit var binding: ActivityAudioplayerBinding

    private lateinit var viewModel: TrackViewModel

    private var mediaPlayerClient: PlayerRepository = Creator.getPlayerRepository(this)

    override fun execute(message: Executor.MediaListener) {
        when (message) {
            Executor.MediaListener.DoButtonEnable -> binding.ibPlay.isEnabled = true
            Executor.MediaListener.ChangeButtonDefault -> binding.ibPlay.setImageResource(R.drawable.button_play)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            TrackViewModel.getViewModelFactory("123")
        )[TrackViewModel::class.java]
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        intent?.extras?.getParcelable<Track>(SearchActivity.TRACK_EXTRA)?.let { track ->
            url = track.previewUrl
            binding.tvTrackName.text = track.trackName
            binding.tvNameGroup.text = track.artistName
            binding.tvRightTimeTrack.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            binding.tvRightAlbumName.text = track.collectionName
            val date = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            ).parse(track.releaseDate)
            binding.tvRightYearTrack.text =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            binding.tvRightTrackGenre.text = track.primaryGenreName
            binding.tvRightTrackCountry.text = track.country

            val density = resources.displayMetrics.density
            val roundedCornersImage = 8 * density
            Glide.with(applicationContext)
                .load(track.coverArtWork)
                .placeholder(R.drawable.no_load_image)
                .transform(RoundedCorners(roundedCornersImage.toInt()))
                .into(binding.ivTrackImage)
        }

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        mediaPlayerClient.preparePlayed(url)

        binding.ibPlay.setOnClickListener {
            when (mediaPlayerClient.getPlayerState()) {
                PlayerState.STATE_PLAYING -> binding.ibPlay.setImageResource(R.drawable.button_play)
                PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> binding.ibPlay.setImageResource(
                    R.drawable.button_stop
                )

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

    private fun updateTimerPlay(): Runnable {
        return object : Runnable {
            override fun run() {
                when (mediaPlayerClient.getPlayerState()) {
                    PlayerState.STATE_PLAYING -> {
                        binding.tvPlayTime.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayerClient.currentPosition())
                        handler?.postDelayed(this, DELAY)
                    }

                    PlayerState.STATE_PAUSED -> {
                        handler?.removeCallbacks(this)
                    }

                    PlayerState.STATE_PREPARED -> {
                        binding.tvPlayTime.text = "00:00"
                        handler?.removeCallbacks(this)
                    }

                    else -> {}
                }
            }
        }
    }
}