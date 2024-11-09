package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.interactor.AddTrackLibraryInteractor
import com.example.playlistmaker.library.domain.interactor.AddTrackLibraryInteractorImpl
import com.example.playlistmaker.library.domain.interactor.DeleteTrackLibraryInteractor
import com.example.playlistmaker.library.domain.interactor.DeleteTrackLibraryInteractorImpl
import com.example.playlistmaker.library.domain.interactor.GetTrackLibraryInteractor
import com.example.playlistmaker.library.domain.interactor.GetTrackLibraryInteractorImpl
import com.example.playlistmaker.library.domain.interactor.IsFavoriteCheckInteractor
import com.example.playlistmaker.library.domain.interactor.IsFavoriteCheckInteractorImpl
import com.example.playlistmaker.library.domain.interactor.OnFavoriteClickInteractor
import com.example.playlistmaker.library.domain.interactor.OnFavoriteClickInteractorImpl
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractor
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractorImpl
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractorImpl
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

    singleOf(::AddTrackLibraryInteractorImpl) bind AddTrackLibraryInteractor::class

    singleOf(::DeleteTrackLibraryInteractorImpl) bind DeleteTrackLibraryInteractor::class

    singleOf(::GetTrackLibraryInteractorImpl) bind GetTrackLibraryInteractor::class

    singleOf(::OnFavoriteClickInteractorImpl) bind OnFavoriteClickInteractor::class

    singleOf(::IsFavoriteCheckInteractorImpl) bind IsFavoriteCheckInteractor::class
}
