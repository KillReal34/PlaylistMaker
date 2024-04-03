package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.data.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.api.SimplePlayer

class TrackViewModel(
    val playerTrack: PlayerTrack,
    private val player: SimplePlayer,
) : ViewModel(), SimplePlayer.Listener {
    private val playerStateMutableLiveData = MutableLiveData(PlayerState.CREATED)

    val playerStateLiveData: LiveData<PlayerState>
        get() = playerStateMutableLiveData

    val currentPlayerState: PlayerState?
        get() = playerStateLiveData.value

    val currentPosition: Int
        get() = player.currentPosition

    init {
        addCloseable(player::release)
    }

    override fun onPrepare() = playerStateMutableLiveData.postValue(PlayerState.PREPARED)

    override fun onCompletion() = playerStateMutableLiveData.postValue(PlayerState.PREPARED)

    fun preparePlayer() = player.prepare(url = playerTrack.previewUrl, listener = this)

    fun playbackControl() {
        playerStateMutableLiveData.value = with(player) {
            when (currentPlayerState) {
                PlayerState.CREATED,
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

                TrackViewModel(
                    playerTrack = playerTrack,
                    player = AndroidMediaPlayer(),
                )
            }
        }
    }
}
