package com.example.playlistmaker.playlists.data

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.PlaylistDbConvertor
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.db.TrackInPlaylistEntity
import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.playlists.domain.Playlist
import com.example.playlistmaker.playlists.domain.PlaylistsRepository
import com.example.playlistmaker.playlists.domain.TrackStringGetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val application: Application
) : PlaylistsRepository {

    override fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun updatePlaylist(playlist: Playlist, track: TrackFromAPI): Boolean {
        val playlistId = appDatabase.linkPlaylistTrackDao().checkTrackInPlaylist(track.trackId.toInt(), playlist.id)
        if (playlistId != 0) return false
        playlist.apply {
            numberOfTracks += 1
        }
        appDatabase.playlistDao().updatePlaylist(
            playlist.id,
            playlist.numberOfTracks
        )
        appDatabase.trackInPlaylistDao().addTrack(
            playlistDbConvertor.map(track)
        )
        appDatabase.linkPlaylistTrackDao().insertLink(
            playlistDbConvertor.toLinkPlaylistTrackEntity(playlist.id, track.trackId.toInt())
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

    override suspend fun getPlaylist(playlistId: Int): Playlist? {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        return if (playlist == null) null else playlistDbConvertor.map(playlist)
    }
    override suspend fun getTrackOfPlaylist(playlistId: Int): List<TrackFromAPI> {
        val tracksId = appDatabase.linkPlaylistTrackDao().getTracksOfPlaylist(playlistId)
        val trackInPlaylistEntity = appDatabase.trackInPlaylistDao().getTracksOfPlaylist(tracksId)
        //return convertFromTrackInPlaylistEntity(trackInPlaylistEntity)
        val tracksInPlaylist = convertFromTrackInPlaylistEntity(trackInPlaylistEntity)
        val sortedTrackList = emptyList<TrackFromAPI>().toMutableList()
        tracksId.forEach { id ->
            tracksInPlaylist.forEach { track ->
                if (track.trackId.toInt() == id) sortedTrackList += listOf(track)
            }
        }
        return sortedTrackList
    }

    override suspend fun getTotalDuration(playlistId: Int): Long {
        var totalDurationMillis = 0L
        val tracks = getTrackOfPlaylist(playlistId)
        tracks.forEach { track ->
            totalDurationMillis += track.trackTimeMillis.toLong()
        }
        return totalDurationMillis
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        val playlist = getPlaylist(playlistId)
        val trackCount = playlist!!.numberOfTracks - 1
        appDatabase.playlistDao().updatePlaylist(playlistId, trackCount)
        val linkPlaylistTrackEntity = playlistDbConvertor.toLinkPlaylistTrackEntity(playlistId, trackId)
        appDatabase.linkPlaylistTrackDao().deleteTrackFromPlaylist(linkPlaylistTrackEntity)
        if (!isTrackInPlaylists(trackId)) appDatabase.trackInPlaylistDao().deleteTrack(trackId)
    }

    override suspend fun sharePlaylist(playlistId: Int): Boolean {
        val tracks = getTrackOfPlaylist(playlistId)

        if (tracks.isEmpty()) return false

        val numberOfTracks = TrackStringGetter().getTrackString(tracks.size)
        var number = 0
        var stringTracks = ""
        tracks.forEach { track ->
            number += 1
            stringTracks += "\n${number}. ${track.artistName} - ${track.trackName} (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)})"
        }
        val playlistText = "$numberOfTracks$stringTracks"
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, playlistText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)

        return true
    }

    override suspend fun deletePlaylist(playlist: Playlist, trackList: List<TrackFromAPI>) {
        trackList.forEach { track ->
            deleteTrackFromPlaylist(playlist.id, track.trackId.toInt())
        }
        appDatabase.playlistDao().deletePlaylist(
            playlistDbConvertor.map(playlist)
        )
    }

    override suspend fun updateNewPlaylist(playlist: Playlist) {
        val playListEntity = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().updateNewPlaylist(playListEntity)
    }

    private fun isTrackInPlaylists(trackId: Int): Boolean {
        val check = appDatabase.linkPlaylistTrackDao().isTrackInPlaylists(trackId)
        return check > 0
    }
    private fun convertFromTrackInPlaylistEntity(trackEntities: List<TrackInPlaylistEntity>): List<TrackFromAPI> {
        return trackEntities.map { playlistDbConvertor.map(it) }
    }
}