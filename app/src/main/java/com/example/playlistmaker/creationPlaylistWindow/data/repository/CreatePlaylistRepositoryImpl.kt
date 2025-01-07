package com.example.playlistmaker.creationPlaylistWindow.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.creationPlaylistWindow.data.db.PlaylistEntity
import com.example.playlistmaker.creationPlaylistWindow.domain.repository.CreatePlaylistRepository
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.data.TrackPlaylistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CreatePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context
): CreatePlaylistRepository {
    override fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long> = flow {
        val plailist = appDatabase.playlistDao().insertPlaylist(playlist)
        emit(plailist)
    }

    override fun addTrackInPlaylist(track: TrackPlaylistEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.trackPlaylistDao().insertTrackFromPlaylist(track)
        }
    }

    override fun updatePlaylist(playlist: PlaylistEntity): Flow<Int> = flow {
        val updater = appDatabase.playlistDao().updatePlaylist(playlist)
        emit(updater)
    }

    override fun savePlaylist(uri: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Album")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val zonedDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss")
        val fileName = formatter.format(zonedDateTime) + ".jpg"
        val file = File(filePath, fileName)

        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outStream)
        return file.toUri().toString()
    }

    override fun deletePlaylistById(playlistId: Long): Flow<Long> = flow{
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.playlistDao().deletePlaylistById(playlistId)
        }
    }

}