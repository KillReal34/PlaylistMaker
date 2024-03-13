package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerState

interface MediaPlayerClient {
    fun preparePlayed(url: String)

    fun startPlay()

    fun pausedPlay()

    fun playbackControl()

    fun getPlayerState(): PlayerState

    fun release()

    fun currentPosition(): Int
}