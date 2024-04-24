package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.repository.SimplePlayerRepository

class SimplePlayerRepositoryImpl(
    private val playerFactory: () -> SimplePlayer,
) : SimplePlayerRepository {
    override fun get(): SimplePlayer = playerFactory()
}