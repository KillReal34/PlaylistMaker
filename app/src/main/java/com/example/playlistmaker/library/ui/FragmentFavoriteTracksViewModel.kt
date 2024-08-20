package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.interactor.GetTrackLibraryInteractor
import kotlinx.coroutines.launch

class FragmentFavoriteTracksViewModel(
    private val getTrackLibraryInteractor: GetTrackLibraryInteractor
): ViewModel() {

    private val favoriteStateMutableLiveData = MutableLiveData<FavoriteTracksState>()

    val favoriteStateLiveData: LiveData<FavoriteTracksState>
        get() = favoriteStateMutableLiveData

    fun getFavoriteTrackList() {
        viewModelScope.launch {
            setState(FavoriteTracksState.Load)
            getTrackLibraryInteractor().collect { tracks ->
                if (tracks.isEmpty()) {
                    setState(FavoriteTracksState.isEmpty)
                } else {
                    setState(FavoriteTracksState.Content(tracks))
                }
            }
        }
    }

    private fun setState(state: FavoriteTracksState) {
        favoriteStateMutableLiveData.postValue(state)
    }
}