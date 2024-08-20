package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.player.ui.PlayerTrack
import kotlinx.coroutines.flow.Flow

interface GetTrackLibraryInteractor {
    operator fun invoke(): Flow<List<PlayerTrack>>
}