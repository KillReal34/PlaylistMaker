package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.creationPlaylistWindow.data.converters.PlaylistDatabaseConverter
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.data.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf<SimplePlayer>(::AndroidMediaPlayer)

    factory{ PlaylistDatabaseConverter(get()) }

    factory{Gson()}

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").fallbackToDestructiveMigration()
            .build()
    }

    includes(persistenceModule, networkModule)
}
