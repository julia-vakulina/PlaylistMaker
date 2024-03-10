package com.example.playlistmaker

data class TrackFromAPI(
    val trackName: String ,// Название трека
    val artistName: String , // Имя исполнителя
    val trackTimeMillis: Int , // Продолжительность трека в мс
    val artworkUrl100: String , // Ссылка на изображение обложки
    val collectionName: String , // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String // Страна исполнителя
)

