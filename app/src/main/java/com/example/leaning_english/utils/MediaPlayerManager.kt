package com.example.leaning_english.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MediaPlayerManager {

    private const val BaseVoiceUrl = "https://dict.youdao.com/dictvoice?"

    private var mediaPlayer: MediaPlayer? = null

    fun playSound(type: Int, audio: String){
        if(mediaPlayer == null){
            releasePlayer()
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer!!.reset()
        }
        try {
            val url = BaseVoiceUrl + "type=$type&audio=$audio"
            mediaPlayer?.run {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { mediaPlayer!!.start() }
                setOnCompletionListener {
                    if (mediaPlayer != null){
                        mediaPlayer!!.release()
                        mediaPlayer = null
                    }
                }
            }
        } catch (exception: Exception){
            exception.printStackTrace()
        }
    }

    private fun releasePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}