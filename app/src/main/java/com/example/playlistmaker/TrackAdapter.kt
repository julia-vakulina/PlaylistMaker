package com.example.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

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

        }
    }
    override fun getItemCount() = tracks.size
}