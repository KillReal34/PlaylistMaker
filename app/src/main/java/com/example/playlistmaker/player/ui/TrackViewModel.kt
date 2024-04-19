package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractor

class TrackViewModel(
    val playerTrack: PlayerTrack,
    getSimplePlayerInteractor: GetSimplePlayerInteractor,
) : ViewModel(), SimplePlayer.Listener {
    private val playerStateMutableLiveData = MutableLiveData(PlayerState.CREATED)

    private val player = getSimplePlayerInteractor()

    val playerStateLiveData: LiveData<PlayerState>
        get() = playerStateMutableLiveData

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val playerTrack: PlayerTrack =
                    requireNotNull(savedStateHandle[AudioPlayerActivity.TRACK_EXTRA]) {
                        "Can't get track info from saved state handle by key ${AudioPlayerActivity.TRACK_EXTRA}"
                    }
                val container = ((this[APPLICATION_KEY] as App).dependencyContainerSearchScreen)

                TrackViewModel(
                    playerTrack = playerTrack,
                    getSimplePlayerInteractor = container.getSimplePlayerInteractor,
                )
            }
        }
    }
}
