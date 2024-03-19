package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackViewModel(
    private val trackId: String
) : ViewModel() {
    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackViewModel(trackId) as T
                }
            }
    }
}