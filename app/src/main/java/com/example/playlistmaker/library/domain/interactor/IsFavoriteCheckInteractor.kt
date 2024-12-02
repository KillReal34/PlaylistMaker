package com.example.playlistmaker.library.domain.interactor

interface IsFavoriteCheckInteractor {
    operator suspend fun invoke(trackId: String): Boolean
}