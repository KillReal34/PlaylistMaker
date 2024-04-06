package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.player.domain.entities.SimplePlayer

interface SimplePlayerRepository {
    fun get(): SimplePlayer
}