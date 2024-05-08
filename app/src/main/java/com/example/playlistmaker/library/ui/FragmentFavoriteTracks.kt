package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FragmentFavoriteTracks : Fragment() {

    companion object{
        private const val TRACKS = "track_list"

        fun newInstance(tracks: String) = FragmentFavoriteTracks().apply {
            arguments = Bundle().apply {
                putString(TRACKS, tracks)
            }
        }
    }

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}