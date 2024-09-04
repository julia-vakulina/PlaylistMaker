package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.search.domain.JsonParser
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment: Fragment() {
    private var _binding: ActivityTrackBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerViewModel by viewModel()
    val jsonParser: JsonParser by inject()
    private lateinit var adapter: BottomSheetAdapter
    companion object {
        const val TRACK_TAG = "track"

        fun createArgs(jsonTrack: String): Bundle {
            return bundleOf(
                TRACK_TAG to jsonTrack
            )
        }
    }
    private val jsonTrack by lazy { requireArguments().getString(TRACK_TAG) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = jsonParser.jsonToObject(jsonTrack.toString(), TrackFromAPI::class.java)
        viewModel.setupTrack(track)


        adapter = BottomSheetAdapter(
            object: BottomSheetAdapter.BottomClickListener {
                override fun onPlaylistClick(playlist: Playlist) {
                    viewModel.addTrackToPlaylist(playlist)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        binding.recyclerPlaylists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerPlaylists.adapter = adapter
        viewModel.getBottomSheetLiveData().observe(viewLifecycleOwner) {bottomSheetState ->
            when (bottomSheetState) {
                is BottomSheetState.Loading -> {
                    binding.recyclerPlaylists.visibility = View.GONE
                    binding.progressbarTrack.visibility = View.VISIBLE
                }
                is BottomSheetState.Content -> {
                    binding.recyclerPlaylists.visibility = View.VISIBLE
                    binding.progressbarTrack.visibility = View.GONE
                    adapter.setPlaylists(bottomSheetState.playlists as ArrayList)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetTrack)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.buttonPlusTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        binding.buttonAddPlaylist.setOnClickListener {
            viewModel.onPause()
            findNavController().navigate(R.id.action_playerFragment_to_playlistFragment)
        }

        viewModel.getAddTrackToPlaylistLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            if (it.bottom) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }



        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).
        placeholder(R.drawable.snake).into(binding.imageTrackTrack)
        binding.nameTrackTrack.text = track.trackName
        binding.artistTrackTrack.text = track.artistName
        binding.duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.album.text = track.collectionName
        binding.year.text = track.releaseDate.substring(0,4)
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        binding.buttonPlayTrack.isEnabled = false
        viewModel.getTimerLiveData().observe(viewLifecycleOwner) {
            binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is PlayerState.Default -> {
                    binding.buttonPlayTrack.isEnabled = false
                    binding.buttonPlayTrack.setImageResource(R.drawable.play)
                }
                is PlayerState.Prepared -> {
                    binding.buttonPlayTrack.isEnabled = true
                    binding.buttonPlayTrack.setImageResource(R.drawable.play)
                }
                is PlayerState.Playing -> {
                    binding.buttonPlayTrack.isEnabled = true
                    binding.buttonPlayTrack.setImageResource(if (viewModel.isPlaying()) R.drawable.pause else R.drawable.play)
                }
                is PlayerState.Paused -> {
                    binding.buttonPlayTrack.isEnabled = true
                    binding.buttonPlayTrack.setImageResource(R.drawable.play)
                }
            }
        }



        track.previewUrl?.let { viewModel.preparePlayer(url = it) }

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.playbackControl()
            viewModel.startTimer()
            if (viewModel.isPlaying()) {
                binding.buttonPlayTrack.setImageResource(R.drawable.pause)
            } else {
                binding.buttonPlayTrack.setImageResource(R.drawable.play)
            }

        }


        viewModel.getIsFavoriteLiveData().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite != null) {
                changeFavoriteButton(isFavorite)
                track.isFavorite = isFavorite
            }
        }

        changeFavoriteButton(track.isFavorite)

        binding.buttonFavoriteTrack.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.buttonLeftTrack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
    }
    private fun changeFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) binding.buttonFavoriteTrack.setImageResource(R.drawable.favorite_pressed)
        else binding.buttonFavoriteTrack.setImageResource(R.drawable.favorite)
    }
    fun closePlaylist() {

    }
}