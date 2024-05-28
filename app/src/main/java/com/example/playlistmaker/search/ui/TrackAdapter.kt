package com.example.playlistmaker.search.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerActivity
import com.google.gson.Gson

class TrackAdapter(
    private val tracks: List<TrackFromAPI>,
    sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<TrackViewHolder> () {

    //14
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    //14

    private val searchHistory = SearchHistory(sharedPreferences)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                searchHistory.addTrack(tracks[position])
                val context = holder.itemView.context
                val playerIntent = Intent(context, PlayerActivity::class.java)
                val gson = Gson()
                val json = gson.toJson(tracks[position])
                context.startActivity(playerIntent.putExtra(INTENT_KEY, json))
            }
        }
    }
    override fun getItemCount() = tracks.size
}