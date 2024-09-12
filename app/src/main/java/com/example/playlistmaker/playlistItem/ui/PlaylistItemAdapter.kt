package com.example.playlistmaker.playlistItem.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.ui.TrackViewHolder

class PlaylistItemAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>(){
    var trackList = ArrayList<TrackFromAPI>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.isLongClickable = true

        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(trackList[position])
        }

        holder.itemView.setOnLongClickListener {
            clickListener.onTrackLongClick(trackList[position])
            return@setOnLongClickListener true
        }
    }
    interface TrackClickListener {
        fun onTrackClick(track: TrackFromAPI)
        fun onTrackLongClick(track: TrackFromAPI)
    }
}