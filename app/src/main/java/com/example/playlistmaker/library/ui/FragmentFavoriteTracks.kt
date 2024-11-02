package com.example.playlistmaker.library.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.ui.extensions.toTrackEntity
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.player.ui.PlayerTrack
import com.example.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavoriteTracks : Fragment() {

    companion object{
        private const val TRACKS = "track_list"

        fun newInstance(tracks: String) = FragmentFavoriteTracks().apply {
            arguments = bundleOf(TRACKS to tracks)
        }
    }

    private val viewModel: FragmentFavoriteTracksViewModel by viewModel()

    private var favoriteTracksAdapter = TrackAdapter(onTrackClick = ::onLibraryTrackClick)

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentFavoriteTracksBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteTrackList()

        binding.favoriteListEmpty.isVisible = true
        binding.favoriteRecyclerView.adapter = favoriteTracksAdapter

        viewModel.favoriteStateLiveData.observe(viewLifecycleOwner) {
            execute(it)
        }

    }

    private fun execute(favoriteTracksState: FavoriteTracksState) {
        when (favoriteTracksState) {
            is FavoriteTracksState.Content -> showFavoriteList(trackList = favoriteTracksState
                .tracks.map { it.toTrackEntity() })

            is FavoriteTracksState.Load -> showLoading()
            is FavoriteTracksState.isEmpty -> showFavoriteEmptyList()
        }
    }

    private fun showFavoriteList(trackList: List<Track>) {
        binding.favoriteListEmpty.isGone = true
        binding.favoriteProgressBar.isGone = true
        binding.favoriteRecyclerView.isVisible = true

        favoriteTracksAdapter.trackList = trackList
    }

    private fun showFavoriteEmptyList() {
        withBinding {
            favoriteListEmpty.isVisible = true
            favoriteProgressBar.isGone = true
            favoriteRecyclerView.isGone = true

            with(ivErrorImage) {
                val imageResId =
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        R.drawable.not_found_track_image_night_mode
                    } else {
                        R.drawable.not_found_track_image_light_mode
                    }
                setImageResource(imageResId)
            }
        }
    }

    private fun showLoading() {
        binding.favoriteListEmpty.isGone = true
        binding.favoriteProgressBar.isVisible = true
        binding.favoriteRecyclerView.isGone = true
    }

    private fun createAudioPlayerIntent(track: Track) = Intent(
        requireContext(),
        AudioPlayerActivity::class.java,
    ).apply {
        putExtra(AudioPlayerActivity.TRACK_EXTRA, PlayerTrack(track = track))
    }

    private fun onLibraryTrackClick(track: Track) {
        startActivity(createAudioPlayerIntent(track))
    }

    private inline fun <R> withBinding(action: FragmentFavoriteTracksBinding.() -> R) =
        binding.action()
}