package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.repository.SimplePlayerRepository

class GetSimplePlayerInteractorImpl(
    private val simplePlayerRepository: SimplePlayerRepository,
) : GetSimplePlayerInteractor {
    override fun invoke(): SimplePlayer = simplePlayerRepository.get()
}