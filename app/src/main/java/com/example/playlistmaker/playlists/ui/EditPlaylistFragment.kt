package com.example.playlistmaker.playlists.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playlists.domain.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment: PlaylistFragment() {
    companion object {
        private const val PLAYLIST = "playlist"
        fun createArgs(
            playlist: Playlist
        ): Bundle {
            return bundleOf(
                PLAYLIST to playlist
            )
        }
    }
    private val playlist by lazy { requireArguments().getSerializable(PLAYLIST) }
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var pathToImage: String = ""

    override val viewModel by viewModel<EditPlaylistViewModel> {
        parametersOf(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleNewPlaylist.setText(getString(R.string.edit))
        binding.buttonCreatePlaylist.setText(getString(R.string.save))
        viewModel.getPlaylistInfoLiveData().observe(viewLifecycleOwner) {state ->
            when (state) {
                is EditPlaylistState.PlaylistInfo -> showPlaylistInfo(state)}
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
        binding.buttonLeft.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.playlistNameTextInputLayout.editText?.doAfterTextChanged {
            playlistName = it?.toString().orEmpty()
        }
        binding.playlistDescriptionTextInputLayout.editText?.doAfterTextChanged {
            playlistDescription = it?.toString().orEmpty()
        }
        val pickAlbumImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this).load(uri)
                        .transform(CenterCrop(),RoundedCorners(16))
                        .into(binding.newPlaylistImage)
                    pathToImage = uri.toString()
                }
            }
        binding.newPlaylistImage.setOnClickListener {
            pickAlbumImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.updatePlaylist(playlistName, playlistDescription, pathToImage)
            findNavController().navigateUp()
        }
    }
    fun showPlaylistInfo(state: EditPlaylistState.PlaylistInfo) {
        if (state.playlist.pathToImage.isNotEmpty()) {
            Glide.with(this).load(state.playlist.pathToImage.toUri())
                .transform(CenterCrop(),RoundedCorners(16))
                .into(binding.newPlaylistImage)
        }

        binding.newPlaylistName.setText(state.playlist.playlistName)

        if (!state.playlist.playlistDescription.isNullOrEmpty()) {
            binding.newPlaylistDescription.setText(state.playlist.playlistDescription)
        }
    }
}