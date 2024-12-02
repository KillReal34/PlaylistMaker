package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_EXTRA = "track_extra"
        private val DELAY = 300.milliseconds
    }

    private var timerJob: Job? = null

    private val binding: ActivityAudioplayerBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityAudioplayerBinding.inflate(layoutInflater)
    }

    private val viewModel: TrackViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.preparePlayer()

        viewModel.observeFavoriteTracksLiveData.observe(this) {isFavorite ->
            clickLike(isFavorite)
        }

        withBinding {
            setContentView(root)

            buttonBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            ibPlay.setOnClickListener {
                viewModel.playbackControl()
                updateTimerPlay()
            }

            ibLike.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.playerTrack.let { viewModel.onFavoriteClicked(it) }
                }
            }
        }

        lifecycleScope.launch {
            var isFavorite = viewModel.isTrackFavorite(viewModel.playerTrack.trackId)
            clickLike(isFavorite)
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
        tvPlayTime.text = "00:00"
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

    private fun updateTimerPlay(){
        timerJob = lifecycleScope.launch {
            while (viewModel.currentPlayerState == PlayerState.PLAYING) {
                delay(DELAY.inWholeMilliseconds)
                binding.tvPlayTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(viewModel.currentPosition)
            }
            if (viewModel.currentPlayerState == PlayerState.PREPARED) {
                binding.tvPlayTime.text = "00:00"
            }
        }
    }

    private fun clickLike(isFavoriteStatus: Boolean){
        when(isFavoriteStatus){
            true -> binding.ibLike.setImageResource(R.drawable.button_like_true)
            false -> binding.ibLike.setImageResource(R.drawable.button_like_false)
        }
    }

    private fun <R> withBinding(action: ActivityAudioplayerBinding.() -> R) = binding.action()
}
