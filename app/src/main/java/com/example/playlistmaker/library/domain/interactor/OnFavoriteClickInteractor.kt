package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.domain.entities.Track

interface OnFavoriteClickInteractor {
    operator suspend fun invoke(track: Track)
}