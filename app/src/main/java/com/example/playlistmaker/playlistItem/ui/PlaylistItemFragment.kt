package com.example.playlistmaker.playlistItem.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.playlistItem.domain.MinuteStringGetter
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.TrackStringGetter
import com.example.playlistmaker.playlists.ui.EditPlaylistFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistItemFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistId by lazy { requireArguments().getInt(PLAYLIST) }
    private lateinit var playlist: Playlist
    private val viewModel by viewModel<PlaylistItemViewModel> {
        parametersOf(playlistId)
    }
    private lateinit var adapter: PlaylistItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistItemAdapter(object : PlaylistItemAdapter.TrackClickListener {
            override fun onTrackClick(track: TrackFromAPI) {
                openPlayer(track)
            }

            override fun onTrackLongClick(track: TrackFromAPI) {
                deleteTrack(track)
            }

        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainerSetting)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.menu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.share.setOnClickListener {
            viewModel.sharePlaylist(playlistId)
        }
        binding.shareAdditionalMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist(playlistId)
        }
        binding.editAdditionalMenu.setOnClickListener {
            findNavController().navigate(R.id.action_playlistItemFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist))
        }
        binding.deleteAdditionalMenu.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(getString(R.string.delete_playlist_dialog, playlist.playlistName))
                .setNegativeButton(R.string.no_dialog) { dialog, which ->

                }.setPositiveButton(R.string.yes_dialog) { dialog, which ->
                    viewModel.deletePlaylist(playlist)
                    findNavController().navigateUp()
                }.show()
        }

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        requireActivity().onBackPressedDispatcher.addCallback{
            findNavController().navigateUp()
        }
        binding.buttonLeft.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun render(state: PlaylistItemState) {
        when (state) {
            is PlaylistItemState.Loading -> {}
            is PlaylistItemState.Content -> showPlaylistDetail(state)
            is PlaylistItemState.EmptyShare -> showEmptyShareToast(state.toastTextId)
        }
    }
    private fun showPlaylistDetail(state: PlaylistItemState.Content) {
        playlist = state.playlist
        val duration = state.duration
        val trackList = state.trackList
        binding.playlistTextView.visibility = View.GONE
        if (trackList.isEmpty()) {
            binding.playlistTextView.visibility = View.VISIBLE
            binding.playlistTextView.text = getString(R.string.dont_have_tracks)
        }

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
        Log.d("AAA", playlist.pathToImage)

        Glide.with(this)
            .load(playlist.pathToImage.toUri())
            .placeholder(R.drawable.snake)
            .transform(CenterCrop())
            .into(binding.imagePlaylist)

        binding.playlistName.text = playlist.playlistName
        binding.playlistDescription.text = playlist.playlistDescription
        binding.playlistNumber.text =
            TrackStringGetter().getTrackString(playlist.numberOfTracks)
        binding.playlistTime.text = MinuteStringGetter().getMinuteString(duration.toInt())

        Glide.with(this)
            .load(playlist.pathToImage.toUri())
            .placeholder(R.drawable.snake)
            .transform(CenterCrop(),
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.image_radius_playlist),
                        resources.displayMetrics).toInt()
                )
            )
            .into(binding.playlistImageInMenu)
        binding.playlistNameInMenu.text = playlist.playlistName
        binding.numberInMenu.text = TrackStringGetter().getTrackString(playlist.numberOfTracks)
    }
    private fun showEmptyShareToast(textId: Int) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(textId),
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun openPlayer(track: TrackFromAPI) {
        val gson = Gson()
        val json = gson.toJson(track)
        findNavController().navigate(R.id.action_playlistItemFragment_to_playerFragment,
            PlayerFragment.createArgs(json))
    }
    private fun deleteTrack(track: TrackFromAPI) {
         MaterialAlertDialogBuilder(requireActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle(R.string.delete_track_dialog)
            .setNegativeButton(R.string.no_dialog) { dialog, which ->

            }.setPositiveButton(R.string.yes_dialog) { dialog, which ->
                 viewModel.deleteTrack(track.trackId.toInt())
            }.show()

    }
    override fun onResume() {
        super.onResume()
        viewModel.getScreenState(playlistId)
    }
    companion object {
        fun newInstance() = PlaylistItemFragment()
        private const val PLAYLIST = "playlist"
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST to playlistId)
        }
    }
}