package com.akshar.vibraterhythm.player;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akshar.vibraterhythm.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageButton playPauseButton, nextButton, previousButton;
    private TextView songName;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ArrayList<String> musicListPath;
    private int currentTrackIndex = 0;
    private static final int SEEK_BAR_UPDATE_DELAY = 500;
    private final Handler handler = new Handler();
    private final Runnable seekBarUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(this, SEEK_BAR_UPDATE_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initViews();
        initMediaPlayer();
        getPathsFromIntent();
        handler.postDelayed(seekBarUpdateRunnable, SEEK_BAR_UPDATE_DELAY);
    }

    private void getPathsFromIntent() {
        musicListPath = new ArrayList<>();
        Collections.addAll(musicListPath, Objects.requireNonNull(getIntent().getStringArrayExtra("musicPathArray")));
        currentTrackIndex = getIntent().getIntExtra("musicIndex", 0);
        playMusicAtIndex(currentTrackIndex);
    }

    private void initViews() {
        playPauseButton = findViewById(R.id.playPause);
        nextButton = findViewById(R.id.next);
        previousButton = findViewById(R.id.previous);
        songName = findViewById(R.id.songName);
        seekBar = findViewById(R.id.seekBar);

        playPauseButton.setOnClickListener(v -> playPause());
        nextButton.setOnClickListener(v -> nextMusic());
        previousButton.setOnClickListener(v -> previousMusic());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.pause);
        });
        mediaPlayer.setOnCompletionListener(mp -> nextMusic());
    }

    private void playPause() {
        if (isPlaying) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);
        }
        isPlaying = !isPlaying;
    }

    private void nextMusic() {
        currentTrackIndex = (currentTrackIndex + 1) % musicListPath.size();
        playMusicAtIndex(currentTrackIndex);
    }

    private void previousMusic() {
        currentTrackIndex = (currentTrackIndex - 1 + musicListPath.size()) % musicListPath.size();
        playMusicAtIndex(currentTrackIndex);
    }

    private void playMusicAtIndex(int index) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicListPath.get(index));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e("Music", "Error playing music: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacks(seekBarUpdateRunnable);
    }
}