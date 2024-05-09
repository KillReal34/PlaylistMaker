package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractor
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractorImpl
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractorImpl
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::AddTrackToAuditionHistoryInteractorImpl) bind AddTrackToAuditionHistoryInteractor::class

    singleOf(::ClearSearchHistoryInteractorImpl) bind ClearSearchHistoryInteractor::class

    singleOf(::GetAuditionHistoryFlowInteractorImpl) bind GetAuditionHistoryFlowInteractor::class

    singleOf(::SearchTracksByNameInteractorImpl) bind SearchTracksByNameInteractor::class

    singleOf(::ChangeThemeInteractorImpl) bind ChangeThemeInteractor::class

    singleOf(::GetThemeFlowInteractorImpl) bind GetThemeFlowInteractor::class

    singleOf(::GetSimplePlayerInteractorImpl) bind GetSimplePlayerInteractor::class
}
