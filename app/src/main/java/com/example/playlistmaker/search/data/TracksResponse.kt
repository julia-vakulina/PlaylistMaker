package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.TrackFromAPI

class TracksResponse (
    val searchType: String,
    val expression: String,
    val results: List<TrackDto> ) : Response()