package com.example.playlistmaker.playlists.data

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.PlaylistDbConvertor
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val application: Application
) : PlaylistsRepository {

    override fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun updatePlaylist(playlist: Playlist, track: TrackFromAPI): Boolean {
        if (playlist.tracksIds.contains(track.trackId.toInt())) return false
        playlist.apply {
            tracksIds.add(track.trackId.toInt())
            numberOfTracks += 1
        }
        appDatabase.playlistDao().updatePlaylist(
            playlist.playlistName,
            playlistDbConvertor.idsToJson(playlist.tracksIds),
            playlist.numberOfTracks
        )
        appDatabase.trackInPlaylistDao().addTrack(
            playlistDbConvertor.map(track)
        )
        return true
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convert(playlists))
    }
    private fun convert(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            playlistDbConvertor.map(it)
        }
    }

    override fun saveImageToAppStorage(uri: Uri, name: String): String {
        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        if (!filePath.exists()) { filePath.mkdirs() }
        val file = File(filePath, "$name.jpg")
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri().toString()
    }
}