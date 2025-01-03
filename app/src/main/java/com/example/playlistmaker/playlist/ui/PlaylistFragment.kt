package com.example.playlistmaker.playlist.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    companion object{
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel: PlaylistViewModel by viewModel()
}