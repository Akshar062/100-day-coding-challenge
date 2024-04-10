package com.akshar.vibraterhythm.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akshar.vibraterhythm.R
import java.util.Collections
import java.util.Objects

class MusicPlayerActivity : AppCompatActivity() {
    private var playPauseButton: ImageButton? = null
    private var nextButton: ImageButton? = null
    private var previousButton: ImageButton? = null
    private var songName: TextView? = null
    private var seekBar: SeekBar? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var musicListPath: ArrayList<String>? = null
    private var currentTrackIndex = 0
    private val handler = Handler()
    private val seekBarUpdateRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                seekBar!!.progress = mediaPlayer!!.currentPosition
            }
            handler.postDelayed(this, SEEK_BAR_UPDATE_DELAY.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        initViews()
        initMediaPlayer()
        pathsFromIntent
        handler.postDelayed(seekBarUpdateRunnable, SEEK_BAR_UPDATE_DELAY.toLong())
    }

    private val pathsFromIntent: Unit
        get() {
            musicListPath = ArrayList()
            musicListPath?.let {
                Collections.addAll(
                    it, *Objects.requireNonNull(
                        intent.getStringArrayExtra("musicPathArray")
                    )
                )
            }
            currentTrackIndex = intent.getIntExtra("musicIndex", 0)
            playMusicAtIndex(currentTrackIndex)
        }

    private fun initViews() {
        playPauseButton = findViewById(R.id.playPause)
        nextButton = findViewById(R.id.next)
        previousButton = findViewById(R.id.previous)
        songName = findViewById(R.id.songName)
        seekBar = findViewById(R.id.seekBar)
        playPauseButton!!.setOnClickListener { playPause() }
        nextButton!!.setOnClickListener { nextMusic() }
        previousButton!!.setOnClickListener { previousMusic() }
        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnPreparedListener {
            seekBar!!.setMax(mediaPlayer!!.duration)
            mediaPlayer!!.start()
            isPlaying = true
            playPauseButton!!.setImageResource(R.drawable.pause)
        }
        mediaPlayer!!.setOnCompletionListener { nextMusic() }
    }

    private fun playPause() {
        if (isPlaying) {
            mediaPlayer!!.pause()
            playPauseButton!!.setImageResource(R.drawable.play)
        } else {
            mediaPlayer!!.start()
            playPauseButton!!.setImageResource(R.drawable.pause)
        }
        isPlaying = !isPlaying
    }

    private fun nextMusic() {
        currentTrackIndex = (currentTrackIndex + 1) % musicListPath!!.size
        playMusicAtIndex(currentTrackIndex)
    }

    private fun previousMusic() {
        currentTrackIndex = (currentTrackIndex - 1 + musicListPath!!.size) % musicListPath!!.size
        playMusicAtIndex(currentTrackIndex)
    }

    private fun playMusicAtIndex(index: Int) {
        mediaPlayer!!.reset()
        try {
            mediaPlayer!!.setDataSource(musicListPath!![index])
            mediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
            Log.e("Music", "Error playing music: " + e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
        handler.removeCallbacks(seekBarUpdateRunnable)
    }

    companion object {
        private const val SEEK_BAR_UPDATE_DELAY = 500
    }
}