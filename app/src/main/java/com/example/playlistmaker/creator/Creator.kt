package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.Executor
import com.example.playlistmaker.player.domain.api.PlayerRepository

object Creator {
    fun getPlayerRepository(executor: Executor): PlayerRepository {
        return PlayerRepositoryImpl(executor)
    }
}