package com.example.playlistmaker.domain.api

interface Executor {

    enum class MediaListener{
        DoButtonEnable,
        ChangeButtonDefault
    }

    fun execute(message: MediaListener)
}