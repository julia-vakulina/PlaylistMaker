package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(
    private val tracks: List<TrackFromAPI>,
    sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<TrackViewHolder> () {
    private val searchHistory = SearchHistory(sharedPreferences)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistory.addTrack(tracks[position])
            val context = holder.itemView.context
            val playerIntent = Intent(context, PlayerActivity::class.java)
            val gson = Gson()
            val json = gson.toJson(tracks[position])
            context.startActivity(playerIntent.putExtra(INTENT_KEY, json))
        }
    }
    override fun getItemCount() = tracks.size
}