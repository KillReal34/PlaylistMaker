package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.player.data.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.repository.SimplePlayerRepository

class SimplePlayerRepositoryImpl : SimplePlayerRepository {
    override fun get(): SimplePlayer = AndroidMediaPlayer()
}