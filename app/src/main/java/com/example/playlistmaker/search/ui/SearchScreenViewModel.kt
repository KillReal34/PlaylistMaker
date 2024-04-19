package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor
import kotlinx.coroutines.flow.map

class SearchScreenViewModel(
    private val addTrackToAuditionHistoryInteractor: AddTrackToAuditionHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor,
    private val searchTracksByNameInteractor: SearchTracksByNameInteractor,
    getAuditionHistoryFlowInteractor: GetAuditionHistoryFlowInteractor,
) : ViewModel() {
    private val trackListAuditionHistoryLiveData = getAuditionHistoryFlowInteractor()
        .map { it.trackList }
        .asLiveData()

    private val screenStateMutableLiveData =
        MediatorLiveData<SearchScreenState>(SearchScreenState.Idle)

    init {
        screenStateMutableLiveData
            .addSource(trackListAuditionHistoryLiveData) { auditionHistoryTrackList ->
                val currentScreenState = screenStateLiveData.value ?: return@addSource

                if (currentScreenState !is SearchScreenState.AuditionHistory) return@addSource

                screenStateMutableLiveData.value =
                    currentScreenState.copy(historyTrackList = auditionHistoryTrackList.orEmpty())
            }
    }

    val screenStateLiveData: LiveData<SearchScreenState>
        get() = screenStateMutableLiveData

    fun addTrackToAuditionHistory(track: Track) = addTrackToAuditionHistoryInteractor(track = track)

    fun clearAuditionHistory() = clearSearchHistoryInteractor()

    fun searchTrackByName(namePattern: String) {
        screenStateMutableLiveData.value = SearchScreenState.IsLoading

        searchTracksByNameInteractor(
            namePattern = namePattern,
            onSuccess = { trackList ->
                if (screenStateLiveData.value !is SearchScreenState.IsLoading) {
                    return@searchTracksByNameInteractor
                }

                screenStateMutableLiveData.value =
                    SearchScreenState.SearchedTrackResult(searchedTrackList = trackList)
            },
            onFailure = {
                if (screenStateLiveData.value !is SearchScreenState.IsLoading) {
                    return@searchTracksByNameInteractor
                }

                screenStateMutableLiveData.value = SearchScreenState.OnSearchError
            },
        )
    }

    fun setAuditionHistoryTrack() {
        screenStateMutableLiveData.value = SearchScreenState.AuditionHistory(
            historyTrackList = trackListAuditionHistoryLiveData.value.orEmpty(),
        )
    }

    fun setIdleState() {
        screenStateMutableLiveData.value = SearchScreenState.Idle
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as App).dependencyContainerSearchScreen

                SearchScreenViewModel(
                    clearSearchHistoryInteractor = container.clearSearchHistoryInteractor,
                    addTrackToAuditionHistoryInteractor = container.addTrackToAuditionHistoryInteractor,
                    getAuditionHistoryFlowInteractor = container.getAuditionHistoryFlowInteractor,
                    searchTracksByNameInteractor = container.searchTracksByNameInteractor,
                )
            }
        }
    }
}
