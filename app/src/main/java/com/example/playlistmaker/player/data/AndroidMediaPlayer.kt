package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.entities.SimplePlayer

class AndroidMediaPlayer(
    private val mediaPlayer: MediaPlayer = MediaPlayer(),
) : SimplePlayer {

    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun prepare(
        url: String,
        listener: SimplePlayer.Listener,
    ): Unit = with(mediaPlayer) {
        setDataSource(url)
        prepareAsync()

        setOnPreparedListener { listener.onPrepare() }

        setOnCompletionListener { listener.onCompletion() }
    }

    override fun play() = mediaPlayer.start()

    override fun pause() = mediaPlayer.pause()

    override fun release() = mediaPlayer.release()
}
