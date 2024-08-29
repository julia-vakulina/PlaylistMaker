package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.playlists.domain.Playlist

class BottomSheetAdapter(
    private val clickListener: BottomClickListener
): RecyclerView.Adapter<BottomSheetViewHolder>() {
    private var playlists = ArrayList<Playlist>()
    fun setPlaylists(playlistsList: ArrayList<Playlist>) {
        playlists = playlistsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view2, parent, false)
        return BottomSheetViewHolder(view)
    }
    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlists[position])

        }
    }
    override fun getItemCount(): Int {
        return playlists.size
    }
    fun interface BottomClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}