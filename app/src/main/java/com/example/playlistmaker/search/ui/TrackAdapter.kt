package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackFromAPI

class TrackAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder> () {

    //14
    private var tracks = ArrayList<TrackFromAPI>()
    // создать метод для установки треков setTracks(ArrayList)
    fun setTracks(trackList: ArrayList<TrackFromAPI>){
        tracks = trackList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[position])

        }
    }
    override fun getItemCount() = tracks.size
    fun interface TrackClickListener {
        fun onTrackClick(trackFromAPI: TrackFromAPI)
    }
}