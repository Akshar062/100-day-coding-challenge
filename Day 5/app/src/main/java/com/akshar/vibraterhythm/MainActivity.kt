package com.akshar.vibraterhythm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akshar.vibraterhythm.player.MusicPlayerActivity

class MainActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var musicList: ArrayList<String>? = null
    private var musicListPath: ArrayList<String>? = null
    private var musicAdapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listView = findViewById(R.id.musicList)
        listView!!.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            val intent = Intent(this@MainActivity, MusicPlayerActivity::class.java)
            intent.putExtra("musicPathArray", musicListPath!!.toTypedArray())
            intent.putExtra("musicIndex", position)
            startActivity(intent)
        }
         askReadExternalStoragePermission()

        displayMusic()
    }


    private fun askReadExternalStoragePermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 1)
        }
    }

    private fun displayMusic() {
        Log.d("Music", "Displaying music files")
        // Initialize the ArrayList to hold music file paths
        musicList = ArrayList()
        musicListPath = ArrayList()

        // Initialize ArrayAdapter to bind musicList to ListView
        musicAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicList!!)

        // Set adapter to ListView
        listView!!.setAdapter(musicAdapter)

        // Retrieve music files from the device's music folder
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA
        )
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val filePath = cursor.getString(0)
                Log.d("Music", filePath)
                val fileName = filePath.substring(filePath.lastIndexOf('/') + 1)
                musicList!!.add(fileName)
                musicListPath!!.add(filePath)
            }
            cursor.close()
            musicAdapter!!.notifyDataSetChanged()
        }
    }
}