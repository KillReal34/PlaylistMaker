package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.interactor.GetTrackLibraryInteractor
import com.example.playlistmaker.player.ui.PlayerTrack
import kotlinx.coroutines.launch

class FragmentFavoriteTracksViewModel(
    private val getTrackLibraryInteractor: GetTrackLibraryInteractor
): ViewModel() {

    private val favoriteStateMutableLiveData = MutableLiveData<FavoriteTracksState>()

    val favoriteStateLiveData: LiveData<FavoriteTracksState>
        get() = favoriteStateMutableLiveData

    fun getFavoriteTrackList() {
        viewModelScope.launch{
//            getTrackLibraryInteractor()
//                .collect{favoriteStateMutableLiveData.postValue(checkingFavorite(it))}
            setState(FavoriteTracksState.Load)
            getTrackLibraryInteractor().collect { tracks ->
                if (tracks.isEmpty()) {
                    setState(FavoriteTracksState.isEmpty)
                } else {
                    checkingFavorite(tracks)
                    setState(
                        FavoriteTracksState.Content(
                            tracks.map(::PlayerTrack)
                        )
                    )
                }
            }
        }
    }

    private fun checkingFavorite(tracks: List<Track>): List<Track>{
        tracks.forEach { track -> track.isFavorite = true }
        return tracks
    }

    private fun setState(state: FavoriteTracksState) {
        favoriteStateMutableLiveData.postValue(state)
    }
}