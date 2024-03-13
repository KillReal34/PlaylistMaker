package com.example.playlistmaker.player.domain.api

interface Executor {

    enum class MediaListener{
        DoButtonEnable,
        ChangeButtonDefault
    }

    fun execute(message: MediaListener)
}