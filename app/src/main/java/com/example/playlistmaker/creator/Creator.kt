package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.MediaPlayerClientImpl
import com.example.playlistmaker.player.domain.api.Executor
import com.example.playlistmaker.player.domain.api.MediaPlayerClient

object Creator {
    fun getMediaPlayerClient(executor: Executor): MediaPlayerClient {
        return MediaPlayerClientImpl(executor)
    }
}