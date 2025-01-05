package com.example.playlistmaker.creationPlaylistWindow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creationPlaylistWindow.domain.interactor.CreatePlaylistInteractor
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreationPlaylistViewModel(
    private val interactor: CreatePlaylistInteractor
) : ViewModel() {

    var CreateNewPlaylistFragment = false
    var playlistUri = ""
    var playlistDescription = ""
    var playlistName = ""

    private val playlistMutableLiveData = MutableLiveData<PlaylistState?>()
    val playlistLiveData: LiveData<PlaylistState?>
        get() = playlistMutableLiveData

    fun setUri(uri: String) {
        playlistUri = uri
        checkPlaylist()
    }

    fun addNewPlaylist(playlistUri: String) {
        val playlist = Playlist(
            0,
            playlistName,
            playlistDescription.ifEmpty { null },
            if (playlistUri.isNotEmpty()) interactor.savePlaylist(playlistUri) else null,
            mutableListOf(),
            0,
            0,
        )
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addPlaylist(playlist).collect {
                if (it > 0) {
                    playlistMutableLiveData.postValue(PlaylistState.TRUE)
                } else {
                    playlistMutableLiveData.postValue(PlaylistState.FALSE)
                }
            }
        }
        CreateNewPlaylistFragment = false
    }

    fun checkPlaylist() {
        CreateNewPlaylistFragment =
            playlistName.isNotEmpty() || playlistDescription.isNotEmpty() || playlistUri.isNotEmpty()
    }

    fun resetPlaylistState() {
        playlistMutableLiveData.value = null
    }
}

enum class PlaylistState {
    TRUE,
    FALSE
}