package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_EXTRA = "track_extra"
        private val DELAY = 500.milliseconds
    }

    private val handler = Handler(Looper.getMainLooper())

    private val binding: ActivityAudioplayerBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityAudioplayerBinding.inflate(layoutInflater)
    }

    private val viewModel: TrackViewModel by viewModels(factoryProducer = TrackViewModel::Factory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.preparePlayer()

        withBinding {
            setContentView(root)

            buttonBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            ibPlay.setOnClickListener {
                viewModel.playbackControl()
                handler.post(updateTimerPlay())
            }
        }

        bindPlayerTrackInfo(playerTrack = viewModel.playerTrack)

        viewModel.playerStateLiveData.observe(this) { state ->
            with(binding.ibPlay) {
                isEnabled = state != null && state != PlayerState.CREATED

                val imageResId = if (state == PlayerState.PLAYING) {
                    R.drawable.button_stop
                } else {
                    R.drawable.button_play
                }

                setImageResource(imageResId)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.pauseTrack()
    }

    private fun bindPlayerTrackInfo(playerTrack: PlayerTrack): Unit = withBinding {
        tvTrackName.text = playerTrack.trackName
        tvNameGroup.text = playerTrack.artistName
        tvRightTimeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(playerTrack.duration)
        tvRightAlbumName.text = playerTrack.collectionName

        val date = try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .parse(playerTrack.releaseDate)
        } catch (_: ParseException) {
            null
        }

        tvRightYearTrack.text = SimpleDateFormat("yyyy", Locale.getDefault())
            .run { format(date ?: return@run "") }
        tvRightTrackGenre.text = playerTrack.primaryGenreName
        tvRightTrackCountry.text = playerTrack.country

        val shapeRadius = (resources.displayMetrics.density * 8).toInt()

        Glide.with(applicationContext)
            .load(playerTrack.coverArtWork)
            .placeholder(R.drawable.no_load_image)
            .transform(RoundedCorners(shapeRadius))
            .into(ivTrackImage)
    }

    private fun updateTimerPlay(): Runnable {
        return object : Runnable {
            override fun run() {
                if (viewModel.currentPlayerState == PlayerState.PLAYING) {
                    binding.tvPlayTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(viewModel.currentPosition)

                    handler.postDelayed(this, DELAY.inWholeMilliseconds)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    private fun <R> withBinding(action: ActivityAudioplayerBinding.() -> R) = binding.action()
}
