package com.example.playlistmaker.creationPlaylistWindow.ui

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creationPlaylistWindow.domain.interactor.CreatePlaylistInteractor
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import kotlinx.coroutines.launch

class FragmentEditorPlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor,
): CreationPlaylistViewModel(createPlaylistInteractor) {

    var totalPlaylistTime: Int = 0
    var quantityTracks: Int = 0
    var endingMinute: String = "минут"
    override fun checkPlaylist() {
        super.checkPlaylist()
    }

    fun savePlaylist(editePlaylistId: Long?, editPlaylistUri: String?, editTrackList: MutableList<Int>?){
        val playlist = Playlist(
            id = editePlaylistId!!,
            name = playlistName,
            description = playlistDescription.ifEmpty { null },
            uri = if (playlistUri.isNotEmpty()) createPlaylistInteractor.savePlaylist(playlistUri) else editPlaylistUri,
            listIdTracks = editTrackList ?: mutableListOf(),
            totalPlaylistTime = totalPlaylistTime,
            endingMinute = endingMinute,
            quentityTracks = quantityTracks,
        )

        viewModelScope.launch {
            createPlaylistInteractor.updatePlaylist(playlist).collect{}
        }

        createNewPlaylistFragment = false
    }
}