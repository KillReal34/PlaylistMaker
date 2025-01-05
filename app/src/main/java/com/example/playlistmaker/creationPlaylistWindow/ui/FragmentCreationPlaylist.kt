package com.example.playlistmaker.creationPlaylistWindow.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreationWindowPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCreationPlaylist : Fragment() {

    companion object{
        val URIKEY = "uriKey"
    }

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        FragmentCreationWindowPlaylistBinding.inflate(layoutInflater)
    }

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel: CreationPlaylistViewModel by viewModel()

    var playlistUri = ""

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            playlistUri = uri.toString()
            viewModel.setUri(uri.toString())
            showThePlaylistCover(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.CreateNewPlaylistFragment = false

        if (savedInstanceState != null) {
            playlistUri = savedInstanceState.getString(URIKEY, null) ?: ""
        } else {
            playlistUri = viewModel.playlistUri ?: ""
        }

        if (playlistUri.isNotEmpty()) {
            showThePlaylistCover(playlistUri.toUri())
        }

        withBinding {
            nameNewPlaylist.editText?.setText(viewModel.playlistName)

            descriptionNewPlaylist.editText?.setText(viewModel.playlistDescription)
        }

        viewModel.playlistLiveData.observe(viewLifecycleOwner) {state ->
            when (state) {
                PlaylistState.TRUE -> {
                    findNavController().popBackStack()
                    viewModel.resetPlaylistState()
                    val playlistName = viewModel.playlistName
                    Toast.makeText(requireContext(), "Плейлист \"$playlistName\" создан", Toast.LENGTH_SHORT).show()                }
                PlaylistState.FALSE -> {
                    Toast.makeText(requireContext(), "Не удалось создать плейлист", Toast.LENGTH_SHORT).show()
                }
                null -> {
                }
            }
        }

        withBinding {
            createPlaylistButtom.isEnabled = false

            nameNewPlaylist.editText?.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    createPlaylistButtom.isEnabled = !s.isNullOrEmpty()
                    viewModel.playlistName = s.toString()
                    viewModel.checkPlaylist()
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            pickImage.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            createPlaylistButtom.setOnClickListener {
                savePlaylist(playlistUri)
            }

            buttonBack.setOnClickListener {
                showExitConfirmationDialogFromBackButton()
            }
        }
        backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showExitConfirmationDialogFromBackButton()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(URIKEY, playlistUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }

    fun savePlaylist(playlistUri: String){
        viewModel.addNewPlaylist(playlistUri)
    }

    private fun showExitConfirmationDialogFromBackButton(){
        if (viewModel.CreateNewPlaylistFragment){
            showExitConfirmationDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    fun showThePlaylistCover (uri: Uri?) {
        if (uri != null) {
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.no_load_image_512x512)
                .centerCrop()
                .into(binding.pickImage)
        }
    }

    private fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private inline fun <R> withBinding(action: FragmentCreationWindowPlaylistBinding.() -> R) =
        binding.action()
}