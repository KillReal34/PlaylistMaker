package com.example.playlistmaker.library.ui

import com.example.playlistmaker.player.ui.PlayerTrack

interface FavoriteTracksState {
    data class Content(val tracks: List<PlayerTrack>): FavoriteTracksState

    data object isEmpty : FavoriteTracksState

    data object Load : FavoriteTracksState
}