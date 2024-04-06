package com.example.playlistmaker.player.data

interface SimplePlayer {
    val currentPosition: Int

    fun prepare(
        url: String,
        listener: Listener,
    )

    fun play()

    fun pause()

    fun release()

    interface Listener {
        fun onPrepare()

        fun onCompletion()
    }
}
