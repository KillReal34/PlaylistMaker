package com.example.playlistmaker.domain.api

import android.widget.ImageButton
import com.example.playlistmaker.domain.model.PlayerState

interface MediaPlayerClient {
    fun preparePlayed(url: String)

    fun startPlay()

    fun pausedPlay()

    fun playbackControl()

    fun getPlayerState(): PlayerState

    fun release()

    fun currentPosition(): Int
}