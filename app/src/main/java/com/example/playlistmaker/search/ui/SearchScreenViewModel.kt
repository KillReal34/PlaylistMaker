package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.search.domain.interactor.AddTrackToAuditionHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.ClearSearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.GetAuditionHistoryFlowInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTracksByNameInteractor
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val addTrackToAuditionHistoryInteractor: AddTrackToAuditionHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor,
    private val searchTracksByNameInteractor: SearchTracksByNameInteractor,
    getAuditionHistoryFlowInteractor: GetAuditionHistoryFlowInteractor,
) : ViewModel() {


    private var trackListAuditionHistoryLiveData = getAuditionHistoryFlowInteractor()
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

        viewModelScope.launch {
            val state = searchTracksByNameInteractor
                .runCatching { invoke(namePattern = namePattern) }
                .getOrNull()
                ?.let(SearchScreenState::SearchedTrackResult)
                ?: SearchScreenState.OnSearchError

            if (screenStateLiveData.value !is SearchScreenState.IsLoading) {
                return@launch
            }

            screenStateMutableLiveData.value = state
        }
    }

    fun setAuditionHistoryTrack() {
        screenStateMutableLiveData.value = SearchScreenState.AuditionHistory(
            historyTrackList = trackListAuditionHistoryLiveData.value.orEmpty(),
        )
    }

    fun setIdleState() {
        screenStateMutableLiveData.value = SearchScreenState.Idle
    }
}
