package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creationPlaylistWindow.domain.interactor.CreatePlaylistInteractor
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.SelectedPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val createPlaylistInteractor: CreatePlaylistInteractor,
    private val selectedPlaylistInteractor: SelectedPlaylistInteractor,
) : ViewModel() {

    private val playlist: Playlist? = null

    private var playlistMutableLiveData = MutableLiveData<Playlist?>(playlist)
    fun playlistLiveData(): LiveData<Playlist?> = playlistMutableLiveData

    private var tracksFromPlaylistMutableLiveData = MutableLiveData(listOf<Track>())
    fun tracksFromPlaylistLiveData(): LiveData<List<Track>> = tracksFromPlaylistMutableLiveData

    private var deletePlaylistMutableLiveData = MutableLiveData<DeletePlaylistState>()
    fun deletePlaylistLiveData(): LiveData<DeletePlaylistState> = deletePlaylistMutableLiveData

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedPlaylistInteractor.getPlaylistById(playlistId).collect { playlist ->
                playlistMutableLiveData.postValue(playlist)
                selectedPlaylistInteractor.getTracksFromPlaylist(playlist.listIdTracks)
                    .collect { tracksFromPlaylistMutableLiveData.postValue(it) }
            }
        }
    }

    fun getTracksFromPlaylist(playlistIdList: List<Int>) {
        viewModelScope.launch {
            selectedPlaylistInteractor.getTracksFromPlaylist(playlistIdList)
                .collect { tracksFromPlaylistMutableLiveData.postValue(it) }
        }
    }

    fun DurationPlaylist(trackList: List<Track>): String {
        var duration = 0.0f

        for (track: Track in trackList) {
            val minutes = (track.duration.inWholeMilliseconds) / 60000
            val seconds = (track.duration.inWholeMilliseconds) % 60000 / 1000

            val formattedTime = "%02d:%02d".format(minutes, seconds)

            val srtList = formattedTime.split(":")
            val min = srtList[0].toInt()
            val sec = srtList[1].toInt()
            duration += min + sec / 60.0f
        }
        return rightEndingMinutes(duration.toInt())
    }

    fun updatePlaylist(playlist: Playlist, track: Track) {
        val trackId = track.trackId
        playlist.listIdTracks.remove(trackId.toInt())
        viewModelScope.launch {
            createPlaylistInteractor.updatePlaylist(playlist).collect {}
        }
    }

    fun deletePlaylistById(playlistId: Long) {
        viewModelScope.launch {
            createPlaylistInteractor.deletePlaylistById(playlistId).collect { stateDelete ->
                if (stateDelete == 1L) deletePlaylistMutableLiveData.postValue(
                    DeletePlaylistState(
                        state = true
                    )
                )
            }
        }
    }

    fun deleteTrackById(trackId: Int) {
        viewModelScope.launch{
            playlistInteractor.getPlaylist().collect{
                if (checkTrack(trackId, it)) {
                    playlistInteractor
                }
            }
        }
    }

    fun rightEndingTrack(quantityTracks: Int): String{
        val ending = when (quantityTracks % 10) {
            1 -> " трек"
            2, 3, 4, -> " трека"
            else -> " треков"
        }
        return quantityTracks.toString() + ending
    }

    fun rightEndingMinutes(count: Int): String {
        val str = when (count % 10) {
            1 -> " минута"
            2, 3, 4 -> " минуты"
            else -> " минут"
        }
        return count.toString() + str
    }

    private fun checkTrack(trackId: Int, playlists: List<Playlist>): Boolean{
        for (playlist: Playlist in playlists) {
            if (!playlist.listIdTracks.none{it == trackId}) return false
        }
        return true
    }
}