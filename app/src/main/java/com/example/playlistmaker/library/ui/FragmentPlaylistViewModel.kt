package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.library.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.launch

class FragmentPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val playlistMutableLiveData = MutableLiveData(listOf<Playlist>())
    val playlistLiveData: LiveData<List<Playlist>>
        get() = playlistMutableLiveData

    fun getPlaylist(){
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect{playlistMutableLiveData.postValue(it)}
        }
    }
}