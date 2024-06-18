package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.domain.JsonParser
import com.example.playlistmaker.search.ui.INTENT_KEY
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {
    private lateinit var play: ImageView
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var trackTime: TextView
    val jsonParser: JsonParser by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

         trackTime = findViewById<TextView>(R.id.track_time)
        val trackImage = findViewById<ImageView>(R.id.imageTrack_Track)
        val trackName = findViewById<TextView>(R.id.nameTrack_Track)
        val trackArtist = findViewById<TextView>(R.id.artistTrack_Track)
        val trackDuration = findViewById<TextView>(R.id.duration)
        val trackAlbum = findViewById<TextView>(R.id.album)
        val trackYear = findViewById<TextView>(R.id.year)
        val trackGenre = findViewById<TextView>(R.id.genre)
        val trackCountry = findViewById<TextView>(R.id.country)

        val json = intent.getStringExtra(INTENT_KEY)
        val track = jsonParser.jsonToObject(json.toString(), TrackFromAPI::class.java)



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



        viewModel.preparePlayer(url = track.previewUrl)

        play.setOnClickListener {
            viewModel.playbackControl()
            viewModel.startTimer()
            if (viewModel.isPlaying()) {
                play.setImageResource(R.drawable.pause)
            } else {
                play.setImageResource(R.drawable.play)
            }

        }



        val buttonLeft = findViewById<Button>(R.id.button_left_track)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.onPause()

    }

}