package com.example.playlistmaker.playlists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.BottomSheetAdapter
import com.example.playlistmaker.playlists.domain.Playlist

class PlaylistAdapter(
   private val clickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }
    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlists[position])

        }
    }
    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}