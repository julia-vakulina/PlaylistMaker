package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }
    private var playerState = STATE_DEFAULT
    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private lateinit var trackTime: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        mainThreadHandler = Handler(Looper.getMainLooper())
        trackTime = findViewById(R.id.track_time)

        val trackImage = findViewById<ImageView>(R.id.imageTrack_Track)
        val trackName = findViewById<TextView>(R.id.nameTrack_Track)
        val trackArtist = findViewById<TextView>(R.id.artistTrack_Track)
        val trackDuration = findViewById<TextView>(R.id.duration)
        val trackAlbum = findViewById<TextView>(R.id.album)
        val trackYear = findViewById<TextView>(R.id.year)
        val trackGenre = findViewById<TextView>(R.id.genre)
        val trackCountry = findViewById<TextView>(R.id.country)


        val gson = Gson()
        val json = intent.getStringExtra(INTENT_KEY)
        val track = gson.fromJson(json, TrackFromAPI::class.java)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).
            placeholder(R.drawable.snake).into(trackImage)
        trackName.text = track.trackName
        trackArtist.text = track.artistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.releaseDate.substring(0,4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
        var url: String? = track.previewUrl

        play = findViewById(R.id.button_play_track)
        preparePlayer(url)
        play.setOnClickListener {
            playbackControl()
        }


        val buttonLeft = findViewById<Button>(R.id.button_left_track)
        buttonLeft.setOnClickListener {
            this.onBackPressed()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            trackTime.text = "00:00"
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun startTimer() {
        mainThreadHandler?.post(updateTimer())
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }
}