package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.data.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf<SimplePlayer>(::AndroidMediaPlayer)

    single{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    includes(persistenceModule, networkModule)
}
