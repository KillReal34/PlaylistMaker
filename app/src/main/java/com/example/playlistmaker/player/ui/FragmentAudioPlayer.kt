package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.library.ui.extensions.toTrackEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class FragmentAudioPlayer : Fragment() {
    companion object {
        fun createArgs(playerTrack: PlayerTrack): Bundle = bundleOf(TRACK_EXTRA to playerTrack)
        const val TRACK_EXTRA = "track_extra"
        private val DELAY = 300.milliseconds
    }

    private val playlists = arrayListOf<Playlist>()
    private var timerJob: Job? = null

    private val binding: FragmentAudioPlayerBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentAudioPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var adapter: BottomSheetAdapter

    private val viewModel: TrackViewModel by viewModel()

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.preparePlayer()

        viewModel.observeFavoriteTracksLiveData.observe(viewLifecycleOwner) {isFavorite ->
            clickLike(isFavorite)
        }

        adapter = BottomSheetAdapter{ playlist ->
            if (clickDebounce()){
                viewModel.updatePlaylist(playlist, viewModel.playerTrack.toTrackEntity())
            }
        }

        adapter.playlists = playlists

        withBinding {

            buttonBack.setOnClickListener { findNavController().popBackStack() }

            ibPlay.setOnClickListener {
                viewModel.playbackControl()
                updateTimerPlay()
            }

            rvBottomSheet.adapter = adapter

            ibLike.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.playerTrack.let { viewModel.onFavoriteClicked(it) }
                }
            }

            val bottomSheetContainer = playlistsBottomSheet
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer as View).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlists ->
            adapter.updatePlaylists(playlists)
        }

        viewModel.addTrackResultLiveData.observe(viewLifecycleOwner) {    (isAdded, playlistName) ->
            if (isAdded) {
                Toast.makeText(requireContext(), "Добавлено в плейлист \"${playlistName}\"", Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                Toast.makeText(requireContext(), "Трек уже добавлен в плейлист \"$playlistName\"", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isGone = true
                    } else -> {
                    binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val alpha = (slideOffset + 1) / 2
                binding.overlay.alpha = alpha
            }
        })

        binding.ibAddPlaylist.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getPlaylist()
                binding.playlistsBottomSheet.isVisible = true
                binding.btNewPlayListBottomSheet.isVisible = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.btNewPlayListBottomSheet.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAudioPlayer_to_creationPlaylist)
        }

        lifecycleScope.launch {
            var isFavorite = viewModel.isTrackFavorite(viewModel.playerTrack.trackId)
            clickLike(isFavorite)
        }

        bindPlayerTrackInfo(playerTrack = viewModel.playerTrack)

        viewModel.playerStateLiveData.observe(viewLifecycleOwner) { state ->
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

        Glide.with(requireContext())
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

    private fun clickDebounce(): Boolean{
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun <R> withBinding(action: FragmentAudioPlayerBinding.() -> R) = binding.action()
}