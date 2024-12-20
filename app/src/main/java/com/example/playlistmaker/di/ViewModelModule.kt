package com.example.playlistmaker.di

import com.example.playlistmaker.creationPlaylistWindow.ui.CreationPlaylistViewModel
import com.example.playlistmaker.library.ui.FragmentFavoriteTracksViewModel
import com.example.playlistmaker.library.ui.FragmentPlaylistViewModel
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.player.ui.TrackViewModel
import com.example.playlistmaker.search.ui.SearchScreenViewModel
import com.example.playlistmaker.settings.ui.SettingsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainActivityViewModel)

    viewModelOf(::SearchScreenViewModel)

    viewModelOf(::SettingsScreenViewModel)

    viewModelOf(::FragmentPlaylistViewModel)

    viewModelOf(::FragmentFavoriteTracksViewModel)

    viewModelOf(::TrackViewModel)

    viewModelOf(::CreationPlaylistViewModel)
}
