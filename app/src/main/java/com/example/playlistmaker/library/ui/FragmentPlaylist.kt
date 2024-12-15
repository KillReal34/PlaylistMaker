package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentPlaylist : Fragment() {

    companion object {
        private const val PLAYLISTS = "playLists"

        fun newInstance(playlists: String) = FragmentPlaylist().apply {
            arguments = bundleOf(PLAYLISTS to playlists)
        }
    }

    private val viewModel: FragmentPlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentPlaylistBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            with(ivErrorImage) {
                val imageResId =
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        R.drawable.not_found_track_image_night_mode
                    } else {
                        R.drawable.not_found_track_image_light_mode
                    }

                setImageResource(imageResId)
            }
            btnNewPlaylist.setOnClickListener{
                findNavController().navigate(R.id.action_libraryFragment_to_creationPlaylist)
            }

            adapter = PlaylistAdapter{}

            rvPlaylists.adapter = adapter
            rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        viewModel.getPlaylist()

        viewModel.playlistLiveData.observe(viewLifecycleOwner) {listPlaylists ->
            if (listPlaylists.isNotEmpty()){
                showPlaylist()
                adapter.updatePlaylists(listPlaylists)
            } else {
                showPlaceHolder()
            }
        }
    }

    private fun showPlaceHolder(){
        withBinding {
            rvPlaylists.isGone = true
            ivErrorImage.isVisible = true
            tvErrorText.isVisible = true
        }
    }

    private fun showPlaylist(){
        withBinding {
            rvPlaylists.isVisible = true
            ivErrorImage.isGone = true
            tvErrorText.isGone = true
        }
    }
    private inline fun <R> withBinding(action: FragmentPlaylistBinding.() -> R) = binding.action()
}