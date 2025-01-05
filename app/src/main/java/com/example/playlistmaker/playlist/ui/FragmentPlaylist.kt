package com.example.playlistmaker.playlist.ui

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentSelectedPlaylistBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.player.ui.FragmentAudioPlayer
import com.example.playlistmaker.player.ui.PlayerTrack
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentPlaylist : Fragment() {

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val binding: FragmentSelectedPlaylistBinding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentSelectedPlaylistBinding.inflate(layoutInflater)
    }

    private val viewModel: PlaylistViewModel by viewModel()

    lateinit var adapter: PlaylistAdapter

    private var isClickAllowed: Boolean = true
    private val playlistId: Long by lazy { requireArguments().getLong(ARGS_PLAYLIST_ID) }
    private var playlist: Playlist? = null
    private var trackList = ArrayList<Track>()
    val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylistById(playlistId)
        viewModel.playlistLiveData().observe(viewLifecycleOwner) {
            playlist = it
            if (playlist != null) showPlaylist(playlist!!)
        }

        viewModel.tracksFromPlaylistLiveData().observe(viewLifecycleOwner) { trackList ->
            withBinding {
                durationAllTracks.text = viewModel.DurationPlaylist(trackList)
                numberOfTracks.text = viewModel.rightEndingTrack(trackList.size)
                quantityTracks.text = viewModel.rightEndingMinutes(trackList.size)
            }
            showTrack(trackList)
        }

        val bottomSheetContainer = binding.tracksBottomSheet
        val bottomSheetBehavior =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = true
                    }

                    else -> binding.overlay.isGone = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        }
        )

        val shareButton = binding.share
        shareButton.post {
            val shareButtonHeight = shareButton.height
            val offsetFromButton = 24 * resources.displayMetrics.density.toInt()
            val availableHeight =
                resources.displayMetrics.heightPixels - shareButton.top - shareButtonHeight - offsetFromButton

            bottomSheetBehavior.peekHeight = availableHeight
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val editingBottomSheetContainer = binding.editingBottomSheet
        val editingBottomSheetBehavior =
            BottomSheetBehavior.from(editingBottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        editingBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = true
                    }

                    else -> binding.overlay.isGone = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        adapter = PlaylistAdapter({ track ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_playlistFragment_to_fragmentAudioPlayer,
                    FragmentAudioPlayer.createArgs(PlayerTrack(track))
                )
            }
        }, { track ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_track)
                .setMessage(R.string.agree_delete_track)
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    playlist?.let {
                        trackList.remove(track)
                        viewModel.updatePlaylist(it, track)
                    }
                    viewModel.deleteTrackById(track.trackId.toInt())
                    playlist?.listIdTracks?.let { viewModel.getTracksFromPlaylist(it) }
                }.show()
            true
        })
        adapter.tracks = trackList

        withBinding {
            playlistBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            rvTrackList.adapter = adapter
            rvTrackList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            share.setOnClickListener {
                sharePlaylist()
            }

            deletePlaylist.setOnClickListener {
                deletePlaylist()
            }
        }
    }

    private fun deletePlaylist(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.agree_delete_playlist))
            .setNegativeButton(getString(R.string.no)) {_, _, ->}
            .setPositiveButton(getString(R.string.yes)) {_, _, ->
                viewModel.deletePlaylistById(playlistId)
                findNavController().popBackStack()
            }.show()
    }

    private fun sharePlaylist() {
        if (trackList.isEmpty()) {
            Toast.makeText(
                requireContext(),
                R.string.empty_tracks_share_message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var message = playlist?.name + "\n" + (playlist?.description
                ?: "") + "\n" + viewModel.rightEndingTrack(trackList.size) + "\n"
            for (index in trackList.indices) {
                val track = trackList[index]
                val trackTime = dateFormat.format(track.duration.inWholeMilliseconds)
                val string =
                    "\n" + (index + 1).toString() + ". " + track.artistName + " '" + track.trackName + "' " + trackTime
                message += string
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            val chooser = Intent.createChooser(intent, getString(R.string.share_playlist))
            startActivity(chooser)
        }
    }

    private fun showPlaylist(playlist: Playlist) {
        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.no_load_image)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.playerCover)
        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.no_load_image)
            .centerCrop()
            .into(binding.album)
        binding.plName.text = playlist.name
    }

    private fun showTrack(tracks: List<Track>) {
        withBinding {
            if (tracks.isEmpty()) {
                rvTrackList.isGone = true
                noTracksMessage.isVisible = true
            } else {
                rvTrackList.isVisible = true
                noTracksMessage.isGone = true
                trackList.clear()
                trackList.addAll(tracks)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val currentClick = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentClick
    }

    private fun <R> withBinding(action: FragmentSelectedPlaylistBinding.() -> R) = binding.action()
}