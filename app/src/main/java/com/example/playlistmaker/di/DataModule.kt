package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf<SimplePlayer>(::AndroidMediaPlayer)

    includes(persistenceModule, networkModule)
}
