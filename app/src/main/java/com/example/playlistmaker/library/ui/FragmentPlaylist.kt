package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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

    private val fragmentPlaylistViewModel: FragmentPlaylistViewModel by viewModel()

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

        }

    }
    private inline fun <R> withBinding(action: FragmentPlaylistBinding.() -> R) = binding.action()
}