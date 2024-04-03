package com.example.playlistmaker.search.ui

import com.example.playlistmaker.domain.entities.Track

sealed interface SearchScreenState {
    data class SearchedTrackResult(val searchedTrackList: List<Track>) : SearchScreenState

    data object IsLoading : SearchScreenState

    data object OnSearchError : SearchScreenState

    data class AuditionHistory(val historyTrackList: List<Track>) : SearchScreenState

    data object Idle : SearchScreenState
}
