package com.example.playlistmaker.playlists.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var imageIsEmpty: Boolean = true
    private var pathToPlaylistImage: String = ""
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel by viewModel<PlaylistViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreatePlaylist.isEnabled = false
        binding.playlistNameTextInputLayout.editText?.doAfterTextChanged {
            binding.buttonCreatePlaylist.isEnabled = it?.isNotBlank() == true
            playlistName = it?.toString().orEmpty()
        }
        binding.playlistDescriptionTextInputLayout.editText?.doAfterTextChanged {
            playlistDescription = it?.toString().orEmpty()
        }
        val pickAlbumImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.newPlaylistImage.setImageURI(uri)
                    imageIsEmpty = false
                    pathToPlaylistImage = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No image selected")
                }
            }
        binding.newPlaylistImage.setOnClickListener {
            pickAlbumImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.buttonLeft.setOnClickListener {
           requireActivity().onBackPressedDispatcher.onBackPressed()
       }
        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setNeutralButton(R.string.dialog_cancel) { dialog, which ->

            }.setPositiveButton(R.string.dialog_finish) { dialog, which ->

                findNavController().popBackStack()
            }
        requireActivity().onBackPressedDispatcher.addCallback{
            if (binding.playlistNameTextInputLayout.editText?.text?.isEmpty() == false ||
                binding.playlistDescriptionTextInputLayout.editText?.text?.isEmpty() == false ||
                !imageIsEmpty) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }
        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.savePlaylist(playlistName, playlistDescription, pathToPlaylistImage)
            viewModel.renderState()
            findNavController().popBackStack()
        }
        viewModel.getPlaylistIsCreatedLiveData().observe(viewLifecycleOwner) {
            if (pathToPlaylistImage != "")
            viewModel.saveImageToAppStorage(
                    pathToPlaylistImage.toUri(),
                    playlistName
                )
                Toast.makeText(requireContext(), resources.getString(R.string.playlist_created, playlistName), Toast.LENGTH_LONG).show()
        }
    }

}