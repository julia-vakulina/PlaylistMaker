package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.playlists.ui.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment: Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {state ->
            renderState(state)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }
    private fun renderState(state: PlaylistScreenState) {
        when(state) {
            is PlaylistScreenState.Empty -> {
                binding.recyclerView.isVisible = false
                binding.searchProgressbar.isVisible = false
                binding.placeHolderPlaylists.isVisible = true
            }
            is PlaylistScreenState.Loading -> {
                binding.placeHolderPlaylists.isVisible = false
                binding.recyclerView.isVisible = false
                binding.searchProgressbar.isVisible = true
            }
            is PlaylistScreenState.Content -> {
                binding.placeHolderPlaylists.isVisible = false
                binding.searchProgressbar.isVisible = false
                binding.recyclerView.isVisible = true

                adapter.playlists.clear()
                adapter.playlists.addAll(state.playlists)
                adapter.notifyDataSetChanged()
            }
        }
    }
}