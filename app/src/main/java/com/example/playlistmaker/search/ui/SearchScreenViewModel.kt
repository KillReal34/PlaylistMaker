package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.entities.AuditionHistory
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryLiveDataInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor

class SearchScreenViewModel(
    private val addTrackToAuditionHistoryInteractor: AddTrackToAuditionHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor,
    private val searchTracksByNameInteractor: SearchTracksByNameInteractor,
    getAuditionHistoryLiveDataInteractor: GetAuditionHistoryLiveDataInteractor
): ViewModel() {


    val trackListAuditionHistoryLiveData = getAuditionHistoryLiveDataInteractor().map {it.trackList}

    fun addTrackToAuditionHistory(track: Track) {
        addTrackToAuditionHistoryInteractor(track)
    }

    fun clearSearchHistory() {
        clearSearchHistoryInteractor()
    }

    fun searchTrackByName(namePattern: String) {
        searchTracksByNameInteractor(namePattern)
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dependencyContainer = (this[APPLICATION_KEY] as App).dependencyContainerSearchScreen
                SearchScreenViewModel(
                    clearSearchHistoryInteractor = dependencyContainer.clearSearchHistoryInteractor,
                    addTrackToAuditionHistoryInteractor = dependencyContainer.addTrackToAuditionHistoryInteractor,
                    getAuditionHistoryLiveDataInteractor = dependencyContainer.getAuditionHistoryLiveDataInteractor,
                    searchTracksByNameInteractor = dependencyContainer.searchTracksByNameInteractor,
                )
            }
        }
    }
}