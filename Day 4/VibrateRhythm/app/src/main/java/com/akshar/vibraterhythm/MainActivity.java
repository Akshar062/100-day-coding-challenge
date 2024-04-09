package com.akshar.vibraterhythm;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.akshar.vibraterhythm.player.MusicPlayerActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> musicList;
    private ArrayList<String> musicListPath;
    private ArrayAdapter<String> musicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.musicList);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Play the selected music
            String musicPath = musicListPath.get(position);
            Log.d("Music", "Playing music: " + musicPath);
            Intent intent = new Intent(this, MusicPlayerActivity.class);
            intent.putExtra("musicPath", musicPath);
            intent.putExtra("musicPathArray", musicListPath.toArray(new String[0]));
            intent.putExtra("musicIndex", position);
            startActivity(intent);
        });
        displayMusic();
    }


    private void displayMusic() {
        Log.d("Music", "Displaying music files");
        // Initialize the ArrayList to hold music file paths
        musicList = new ArrayList<>();
        musicListPath = new ArrayList<>();

        // Initialize ArrayAdapter to bind musicList to ListView
        musicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicList);

        // Set adapter to ListView
        listView.setAdapter(musicAdapter);

        // Retrieve music files from the device's music folder
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(0);
                Log.d("Music", filePath);
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                musicList.add(fileName);
                musicListPath.add(filePath);
            }
            cursor.close();
            musicAdapter.notifyDataSetChanged();
        }
    }
}