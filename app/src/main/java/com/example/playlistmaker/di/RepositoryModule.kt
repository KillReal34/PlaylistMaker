package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.repository.LibraryRepositoryImpl
import com.example.playlistmaker.library.domain.repository.LibraryRepository
import com.example.playlistmaker.player.data.repository.SimplePlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.SimplePlayerRepository
import com.example.playlistmaker.search.data.repository.AuditionHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuditionHistoryRepositoryImpl) bind AuditionHistoryRepository::class

    singleOf(::TrackRepositoryImpl) bind TrackRepository::class

    singleOf(::ThemeRepositoryImpl) bind ThemeRepository::class

    singleOf(::LibraryRepositoryImpl) bind LibraryRepository::class

    single<SimplePlayerRepository> {
        SimplePlayerRepositoryImpl(playerFactory = ::get)
    }
}
