package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.player.ui.PlayerTrack

interface DeleteTrackLibraryInteractor {
    suspend operator fun invoke(track: PlayerTrack)
}