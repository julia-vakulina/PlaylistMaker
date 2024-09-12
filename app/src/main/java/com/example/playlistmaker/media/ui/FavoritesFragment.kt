package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.ui.INTENT_KEY
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()
    companion object {
        fun newInstance() = FavoritesFragment()
    }
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteTracksAdapter: TrackAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerFavoriteTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoriteTracksAdapter = TrackAdapter(clickListener = {
            openPlayer(it)
        })
        binding.recyclerFavoriteTracks.adapter = favoriteTracksAdapter
        viewModel.fillData()
        viewModel.favoriteTracks.observe(viewLifecycleOwner) {
            render(it)
        }
    }
    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> showContent(state.tracks as ArrayList<TrackFromAPI>)
            is FavoritesState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.recyclerFavoriteTracks.isVisible = false
        binding.placeHolderFavorites.isVisible = true
    }

    private fun showContent(tracks: ArrayList<TrackFromAPI>) {
        binding.recyclerFavoriteTracks.isVisible = true
        binding.placeHolderFavorites.isVisible = false

        favoriteTracksAdapter.setTracks(tracks)
        favoriteTracksAdapter.notifyDataSetChanged()
    }
    fun openPlayer(trackFromAPI: TrackFromAPI) {
        val gson = Gson()
        val json = gson.toJson(trackFromAPI)
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(json))

    }
}