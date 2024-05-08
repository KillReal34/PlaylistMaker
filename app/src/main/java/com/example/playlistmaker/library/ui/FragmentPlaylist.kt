package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding


class FragmentPlaylist : Fragment() {

    companion object {
        private const val PLAYLISTS = "playLists"

        fun newInstance(playlists: String) = FragmentPlaylist().apply {
            arguments = Bundle().apply {
                putString(PLAYLISTS, playlists)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
}