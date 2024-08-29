package com.example.playlistmaker.playlists.ui

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.TrackStringGetter

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.imagePlaylist)
    private val namePlaylist: TextView = itemView.findViewById(R.id.namePlaylist)
    private val numberTracks: TextView = itemView.findViewById(R.id.numberTracks)
    fun bind(playlist: Playlist){
        Glide.with(itemView.context)
            .load(playlist.pathToImage.toUri())
            .placeholder(R.drawable.snake)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        itemView.resources.getDimension(R.dimen.image_radius),
                        itemView.resources.displayMetrics).toInt()
                )
            )
            .into(imagePlaylist)
        namePlaylist.text = playlist.playlistName
        numberTracks.text = TrackStringGetter().getTrackString(playlist.numberOfTracks)
    }
}