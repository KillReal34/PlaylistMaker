package com.example.playlistmaker.creationPlaylistWindow.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.creationPlaylistWindow.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditorPlaylist : FragmentCreationPlaylist() {

    companion object {
        private const val ARGS_PLAYLIST = "playlist_id"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }

    override val viewModel by viewModel<EditorPlaylistViewModel>()

    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = getPlaylist()

        binding.titlePlaylist.text = getString(R.string.edit)
        binding.createPlaylistButtom.text = getString(R.string.save)
        binding.etNamePlaylist.setText(playlist?.name)
        binding.etDescriptionPlaylist.setText(playlist?.description)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)

        showThePlaylistCover(playlist?.uri?.toUri())
    }

    override fun savePlaylist(playlistUri: String) {
        viewModel.savePlaylist(playlist?.id, playlist?.uri, playlist?.listIdTracks)
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun getPlaylist(): Playlist? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireArguments().getParcelable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getParcelable(ARGS_PLAYLIST) as Playlist?
        }
    }
}