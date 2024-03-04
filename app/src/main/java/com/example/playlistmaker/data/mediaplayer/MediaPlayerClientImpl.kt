package com.example.playlistmaker.data.mediaplayer

import android.graphics.drawable.AdaptiveIconDrawable
import android.media.MediaPlayer
import android.widget.ImageButton
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.Executor
import com.example.playlistmaker.domain.api.MediaPlayerClient
import com.example.playlistmaker.domain.model.PlayerState

class MediaPlayerClientImpl(val executor: Executor) : MediaPlayerClient {

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