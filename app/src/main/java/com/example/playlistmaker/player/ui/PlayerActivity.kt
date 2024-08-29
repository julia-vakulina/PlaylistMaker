package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.ui.PlaylistFragment
import com.example.playlistmaker.search.domain.JsonParser
import com.example.playlistmaker.search.ui.INTENT_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {
    private lateinit var play: ImageView
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var trackTime: TextView
    val jsonParser: JsonParser by inject()
    private lateinit var buttonFavorite: ImageView
    private lateinit var adapter: BottomSheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

         trackTime = findViewById(R.id.track_time)
        val trackImage = findViewById<ImageView>(R.id.imageTrack_Track)
        val trackName = findViewById<TextView>(R.id.nameTrack_Track)
        val trackArtist = findViewById<TextView>(R.id.artistTrack_Track)
        val trackDuration = findViewById<TextView>(R.id.duration)
        val trackAlbum = findViewById<TextView>(R.id.album)
        val trackYear = findViewById<TextView>(R.id.year)
        val trackGenre = findViewById<TextView>(R.id.genre)
        val trackCountry = findViewById<TextView>(R.id.country)
        buttonFavorite = findViewById(R.id.button_favorite_track)

        val json = intent.getStringExtra(INTENT_KEY)
        val track = jsonParser.jsonToObject(json.toString(), TrackFromAPI::class.java)


        val recycler = findViewById<RecyclerView>(R.id.recycler_playlists)
        val progressBar = findViewById<ProgressBar>(R.id.progressbar_track)
        val bottomSheet = findViewById<LinearLayout>(R.id.bottomSheetTrack)
        val overlay = findViewById<View>(R.id.overlay)
        val buttonPlusTrack = findViewById<ImageView>(R.id.button_plus_track)
        val buttonAddPlaylist = findViewById<Button>(R.id.button_add_playlist)
        adapter = BottomSheetAdapter(
            object: BottomSheetAdapter.BottomClickListener {
                override fun onPlaylistClick(playlist: Playlist) {
                    viewModel.addTrackToPlaylist(playlist)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter
        viewModel.getBottomSheetLiveData().observe(this) {bottomSheetState ->
            when (bottomSheetState) {
                is BottomSheetState.Loading -> {
                    recycler.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is BottomSheetState.Content -> {
                    recycler.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    adapter.setPlaylists(bottomSheetState.playlists as ArrayList)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        buttonPlusTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        buttonAddPlaylist.setOnClickListener {
            Log.e("AAA", "add playlist")
            if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.player_fragment_container,
                PlaylistFragment.newInstance(),
                "PlaylistFragment"
            )
                .commit()
            }
        }
        viewModel.getAddTrackToPlaylistLiveData().observe(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            if (it.bottom) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.setupTrack(track)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).
        placeholder(R.drawable.snake).into(trackImage)
        trackName.text = track.trackName
        trackArtist.text = track.artistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.releaseDate.substring(0,4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        play = findViewById(R.id.button_play_track)
        play.isEnabled = false
        viewModel.getTimerLiveData().observe(this) {
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }

        viewModel.getPlayerStateLiveData().observe(this) {
            when (it) {
                is PlayerState.Default -> {
                    play.isEnabled = false
                    play.setImageResource(R.drawable.play)
                }
                is PlayerState.Prepared -> {
                    play.isEnabled = true
                    play.setImageResource(R.drawable.play)
                }
                is PlayerState.Playing -> {
                    play.isEnabled = true
                    play.setImageResource(if (viewModel.isPlaying()) R.drawable.pause else R.drawable.play)
                }
                is PlayerState.Paused -> {
                    play.isEnabled = true
                    play.setImageResource(R.drawable.play)
                }
            }
        }



        track.previewUrl?.let { viewModel.preparePlayer(url = it) }

        play.setOnClickListener {
            viewModel.playbackControl()
            viewModel.startTimer()
            if (viewModel.isPlaying()) {
                play.setImageResource(R.drawable.pause)
            } else {
                play.setImageResource(R.drawable.play)
            }

        }


        viewModel.getIsFavoriteLiveData().observe(this) { isFavorite ->
            if (isFavorite != null) {
            changeFavoriteButton(isFavorite)
            track.isFavorite = isFavorite
            }
        }
        
        changeFavoriteButton(track.isFavorite)

        buttonFavorite.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        val buttonLeft = findViewById<Button>(R.id.button_left_track)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }
    }
    private fun changeFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) buttonFavorite.setImageResource(R.drawable.favorite_pressed)
        else buttonFavorite.setImageResource(R.drawable.favorite)
    }
    override fun onPause() {
        super.onPause()
        viewModel.onPause()

    }



}