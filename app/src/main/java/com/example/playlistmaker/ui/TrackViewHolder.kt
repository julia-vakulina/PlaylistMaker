package com.example.playlistmaker.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackFromAPI
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val nameTrack: TextView = itemView.findViewById(R.id.nameTrack)
    private val artistTrack: TextView = itemView.findViewById(R.id.artistTrack)
    private val timeTrack: TextView = itemView.findViewById(R.id.timeTrack)
    private val imageTrack = itemView.findViewById<ImageView>(R.id.imageTrack)

    fun bind(model: TrackFromAPI) {
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.snake).transform(
            RoundedCorners(10)
        ).into(imageTrack)
        nameTrack.text = model.trackName
        artistTrack.text = model.artistName
        timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
    }



}