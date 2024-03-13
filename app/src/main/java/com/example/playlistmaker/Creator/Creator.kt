package com.example.playlistmaker.Creator

import com.example.playlistmaker.data.mediaplayer.MediaPlayerClientImpl
import com.example.playlistmaker.domain.api.Executor
import com.example.playlistmaker.domain.api.MediaPlayerClient

object Creator {
    fun getMediaPlayerClient(executor: Executor): MediaPlayerClient {
        return MediaPlayerClientImpl(executor)
    }
}