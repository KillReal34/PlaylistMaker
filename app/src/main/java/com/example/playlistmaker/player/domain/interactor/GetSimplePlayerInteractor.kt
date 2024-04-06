package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.entities.SimplePlayer

interface GetSimplePlayerInteractor {
    operator fun invoke(): SimplePlayer
}