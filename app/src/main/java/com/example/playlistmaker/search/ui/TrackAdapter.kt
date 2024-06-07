package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.SearchHistoryRepositoryImpl
import com.google.gson.Gson
import com.example.playlistmaker.search.domain.SearchHistoryRepository as SearchHistoryRepository

class TrackAdapter(
    //private val tracks: List<TrackFromAPI>,
    //context: Context
    //sharedPreferences: SharedPreferences
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder> () {

    //14
    private var tracks = ArrayList<TrackFromAPI>()
    // создать метод для установки треков setTracks(ArrayList)
    fun setTracks(trackList: ArrayList<TrackFromAPI>){
        tracks = trackList
    }
    //companion object {
    //    private const val CLICK_DEBOUNCE_DELAY = 1000L
    //}

    //private var isClickAllowed = true

    //private val handler = Handler(Looper.getMainLooper())
    //private fun clickDebounce() : Boolean {
    //    val current = isClickAllowed
    //    if (isClickAllowed) {
    //        isClickAllowed = false
    //        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
    //    }
    //    return current
    //}

    //

    //private val searchHistory = Creator.getHistoryRepository()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[position])
            //if (clickDebounce()) {
            //    searchHistory.putToHistory(tracks[position])
            //    val context = holder.itemView.context
            //    val playerIntent = Intent(context, PlayerActivity::class.java)
            //    val gson = Gson()
            //    val json = gson.toJson(tracks[position])
            //    context.startActivity(playerIntent.putExtra(INTENT_KEY, json))

//            }
        }
    }
    override fun getItemCount() = tracks.size
    fun interface TrackClickListener {
        fun onTrackClick(trackFromAPI: TrackFromAPI)
    }
}