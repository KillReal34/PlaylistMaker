package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creationPlaylistWindow.domain.interactor.CreatePlaylistInteractor
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.library.domain.interactor.IsFavoriteCheckInteractor
import com.example.playlistmaker.library.domain.interactor.OnFavoriteClickInteractor
import com.example.playlistmaker.library.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.library.ui.extensions.toTrackEntity
import com.example.playlistmaker.player.domain.entities.SimplePlayer
import com.example.playlistmaker.player.domain.interactor.GetSimplePlayerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackViewModel(
    savedStateHandle: SavedStateHandle,
    getSimplePlayerInteractor: GetSimplePlayerInteractor,
    val favoriteClickInteractor: OnFavoriteClickInteractor,
    val isFavoriteCheckInteractor: IsFavoriteCheckInteractor,
    val playlistInteractor: PlaylistInteractor,
    val createPlaylistInteractor: CreatePlaylistInteractor,
) : ViewModel(), SimplePlayer.Listener {

    val playerTrack: PlayerTrack = requireNotNull(savedStateHandle[FragmentAudioPlayer.TRACK_EXTRA]) {
        "Can't get track info from saved state handle by key ${FragmentAudioPlayer.TRACK_EXTRA}"
    }

    private val playerStateMutableLiveData = MutableLiveData(PlayerState.CREATED)

    private val favoriteTrackLiveData = MutableLiveData(playerTrack.isFavorite)

    private val playlistMutableLiveData = MutableLiveData(listOf<Playlist>())

    private val addTrackResultMutableLiveData = MutableLiveData<Pair<Boolean, String>>()

    private val updatePlaylistMutableLiverData = MutableLiveData("")

    private val player = getSimplePlayerInteractor()

    val playerStateLiveData: LiveData<PlayerState>
        get() = playerStateMutableLiveData

    val observeFavoriteTracksLiveData: LiveData<Boolean>
        get() = favoriteTrackLiveData

    val playlistLiveData: LiveData<List<Playlist>>
        get() = playlistMutableLiveData

    val addTrackResultLiveData: LiveData<Pair<Boolean, String>>
        get() = addTrackResultMutableLiveData

    val updatePlaylistLiveData: LiveData<String>
        get() = updatePlaylistMutableLiverData

    val currentPlayerState: PlayerState?
        get() = playerStateLiveData.value

    val currentPosition: Int
        get() = player.currentPosition

    var playlistName: String = ""

    fun updatePlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            val isAdded = playlist.listIdTracks.none { it.toString() == track.trackId }
            if (isAdded) {
                playlist.listIdTracks.add(track.trackId.toInt())
                createPlaylistInteractor.updatePlaylist(playlist).collect {
                    if (it == 1) addTrackResultMutableLiveData.postValue(Pair(true, playlist.name ?: ""))
                }
            } else {
                addTrackResultMutableLiveData.postValue(Pair(false, playlist.name ?: ""))
            }
        }
    }

    fun getPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect{playlistMutableLiveData.postValue(it)}
        }
    }

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

    suspend fun isTrackFavorite(trackId: String): Boolean{
        return isFavoriteCheckInteractor(trackId)
    }

    suspend fun onFavoriteClicked(track: PlayerTrack){
        viewModelScope.launch(Dispatchers.IO) {
            favoriteClickInteractor(track.toTrackEntity())
            track.isFavorite = !track.isFavorite
            favoriteTrackLiveData.postValue(track.isFavorite)
        }
    }
}
