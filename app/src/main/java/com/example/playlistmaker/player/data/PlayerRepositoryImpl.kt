package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.Executor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerState

class PlayerRepositoryImpl(val executor: Executor) : PlayerRepository {

    private var playerState: PlayerState? = null

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayed(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            executor.execute(Executor.MediaListener.DoButtonEnable)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            executor.execute(Executor.MediaListener.ChangeButtonDefault)
        }
    }

    override fun startPlay() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausedPlay() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun playbackControl() {
        when(playerState) {
            PlayerState.STATE_PLAYING -> {pausedPlay()}
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {startPlay()}
            else -> {}
        }
    }

    override fun getPlayerState(): PlayerState {
        return playerState ?: PlayerState.STATE_DEFAULT
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}