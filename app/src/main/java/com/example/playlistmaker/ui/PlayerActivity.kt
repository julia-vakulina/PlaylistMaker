package com.example.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.data.TrackGetterImpl
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.TrackGetter
import com.example.playlistmaker.domain.models.TrackFromAPI
import com.example.playlistmaker.presentation.PlayerInteractorImpl
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {


    private lateinit var play: ImageView
    private var mainThreadHandler: Handler? = null
    private var mediaPlayer: PlayerInteractor = PlayerInteractorImpl()

    private lateinit var trackTime: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        trackTime = findViewById(R.id.track_time)
        mainThreadHandler = Handler(Looper.getMainLooper())

        val trackImage = findViewById<ImageView>(R.id.imageTrack_Track)
        val trackName = findViewById<TextView>(R.id.nameTrack_Track)
        val trackArtist = findViewById<TextView>(R.id.artistTrack_Track)
        val trackDuration = findViewById<TextView>(R.id.duration)
        val trackAlbum = findViewById<TextView>(R.id.album)
        val trackYear = findViewById<TextView>(R.id.year)
        val trackGenre = findViewById<TextView>(R.id.genre)
        val trackCountry = findViewById<TextView>(R.id.country)

        val trackGetter: TrackGetter = TrackGetterImpl()
        val track = trackGetter.getTrack(INTENT_KEY,intent)
        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).
        placeholder(R.drawable.snake).into(trackImage)
        trackName.text = track.trackName
        trackArtist.text = track.artistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.releaseDate.substring(0,4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
        mediaPlayer.url = track.previewUrl

        play = findViewById(R.id.button_play_track)

        mediaPlayer.preparePlayer(callback = {play.setImageResource(R.drawable.play)})
        play.setOnClickListener {
            mediaPlayer.playbackControl(
                callback1 = {play.setImageResource(R.drawable.play) },
                callback2 = {play.setImageResource(R.drawable.pause)
                    startTimer()
                }
            )
        }



        val buttonLeft = findViewById<Button>(R.id.button_left_track)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer { play.setImageResource(R.drawable.play) }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun startTimer() {
        mainThreadHandler?.post(updateTimer())
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer.playerState == PlayerInteractorImpl.STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
                    mainThreadHandler?.postDelayed(this, PlayerInteractorImpl.DELAY)
                }
            }
        }
    }
}