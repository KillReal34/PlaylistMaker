package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.interactor.AddTrackLibraryInteractor
import com.example.playlistmaker.library.domain.interactor.DeleteTrackLibraryInteractor
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractor
import kotlinx.coroutines.launch

class TrackViewModel(
    savedStateHandle: SavedStateHandle,
    getSimplePlayerInteractor: GetSimplePlayerInteractor,
    val addTrackLibraryInteractor: AddTrackLibraryInteractor,
    val deleteTrackLibraryInteractor: DeleteTrackLibraryInteractor,
) : ViewModel(), SimplePlayer.Listener {

    val playerTrack: PlayerTrack = requireNotNull(savedStateHandle[AudioPlayerActivity.TRACK_EXTRA]) {
        "Can't get track info from saved state handle by key ${AudioPlayerActivity.TRACK_EXTRA}"
    }

    private val playerStateMutableLiveData = MutableLiveData(PlayerState.CREATED)

    private val favoriteTrackLiveData = MutableLiveData<Boolean>()

    private val player = getSimplePlayerInteractor()

    val playerStateLiveData: LiveData<PlayerState>
        get() = playerStateMutableLiveData

    val observeFavoriteTracksLiveData: LiveData<Boolean>
        get() = favoriteTrackLiveData

    val currentPlayerState: PlayerState?
        get() = playerStateLiveData.value

    val currentPosition: Int
        get() = player.currentPosition

    init {
        addCloseable {
            playerStateMutableLiveData.value = PlayerState.RELEASE
            player.release()
        }
    }

    override fun onPrepare() = playerStateMutableLiveData.postValue(PlayerState.PREPARED)

    override fun onCompletion() = playerStateMutableLiveData.postValue(PlayerState.PREPARED)

    fun preparePlayer() = player.prepare(url = playerTrack.previewUrl, listener = this)

    fun playbackControl() {
        playerStateMutableLiveData.value = with(player) {
            when (currentPlayerState) {
                PlayerState.CREATED,
                PlayerState.RELEASE,
                null -> return

                PlayerState.PREPARED,
                PlayerState.PAUSED -> {
                    play()

                    PlayerState.PLAYING
                }

                PlayerState.PLAYING -> {
                    pause()

                    PlayerState.PAUSED
                }
            }
        }
    }

    fun pauseTrack() {
        if (currentPlayerState != PlayerState.PLAYING) return
        player.pause()
    }

    fun onFavoriteClicked(track: PlayerTrack) {
        viewModelScope.launch {
            if (track.isFavorite) {
                deleteTrackLibraryInteractor(track = track)
            } else {
                addTrackLibraryInteractor(track = track)
            }
            track.isFavorite = !track.isFavorite
            favoriteTrackLiveData.postValue(track.isFavorite)
        }
    }
}
