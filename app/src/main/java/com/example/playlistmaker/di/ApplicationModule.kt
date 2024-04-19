package com.example.playlistmaker.di

import org.koin.dsl.module

val applicationModule = module {
    includes(interactorModule, repositoryModule, dataModule, viewModelModule)
}
