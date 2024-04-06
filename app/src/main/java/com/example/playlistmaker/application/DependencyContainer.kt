package com.example.playlistmaker.application

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryLiveDataInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractorImpl
import com.example.playlistmaker.search.data.persistence.AuditionHistoryPersistence
import com.example.playlistmaker.search.data.persistence.AuditionHistorySharedPreferences
import com.example.playlistmaker.search.domain.repository.AuditionHistoryRepository
import com.example.playlistmaker.search.data.repository.AuditionHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryLiveDataInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractorImpl
import com.example.playlistmaker.settings.data.persistence.ThemePersistence
import com.example.playlistmaker.settings.data.persistence.ThemeSharedPreferences
import com.example.playlistmaker.settings.domain.repository.ThemeRepository
import com.example.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.interactor.ChangeThemeInteractor
import com.example.playlistmaker.settings.domain.interactor.GetThemeFlowInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DependencyContainer(
    key: String,
    sharedPreferences: SharedPreferences
) {

    private val baseITunesUrl = "https://itunes.apple.com"
    val retrofitITunesApi = Retrofit.Builder()
        .baseUrl(baseITunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ITunesApi::class.java)

    private val themePersistence: ThemePersistence = ThemeSharedPreferences(
        themeKey = key,
        sharedPreferences = sharedPreferences,
    )

    private val themeRepository: ThemeRepository =
        ThemeRepositoryImpl(themePersistence = themePersistence)

    private val auditionHistoryPersistence: AuditionHistoryPersistence =
        AuditionHistorySharedPreferences(
            auditionHistoryKey = key,
            sharedPreferences = sharedPreferences,
        )

    private val auditionHistoryRepository: AuditionHistoryRepository =
        AuditionHistoryRepositoryImpl(auditionHistoryPersistence = auditionHistoryPersistence)

    private val trackRepository: TrackRepository =
        TrackRepositoryImpl(retrofitITunesApi)

    val addTrackToAuditionHistoryInteractor: AddTrackToAuditionHistoryInteractor =
        AddTrackToAuditionHistoryInteractorImpl(auditionHistoryRepository = auditionHistoryRepository)

    val clearSearchHistoryInteractor: ClearSearchHistoryInteractor =
        ClearSearchHistoryInteractorImpl(auditionHistoryRepository = auditionHistoryRepository)

    val getAuditionHistoryLiveDataInteractor: GetAuditionHistoryLiveDataInteractor =
        GetAuditionHistoryLiveDataInteractorImpl(auditionHistoryRepository = auditionHistoryRepository)

    val searchTracksByNameInteractor: SearchTracksByNameInteractor =
        SearchTracksByNameInteractorImpl(trackRepository = trackRepository)

    val changeThemeInteractor: ChangeThemeInteractor =
        ChangeThemeInteractorImpl(themeRepository = themeRepository)

    val themeLiveDataInteractor: GetThemeFlowInteractor =
        GetThemeFlowInteractorImpl(themeRepository = themeRepository)
}