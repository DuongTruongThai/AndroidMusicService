package com.example.musicplayerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listData: ArrayList<SongData> = ArrayList()
        listData.add(SongData("aotmusic"))
        listData.add(SongData("ironman3theme"))

        val adapter: MyAdapter = MyAdapter()
        adapter.setData(listData)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.onItemClick = { songData ->
            val intent = Intent(this, MusicPlayerActivity::class.java)
            when(songData.title){
                "aotmusic" ->{
                    intent.putExtra("songTitle",songData.title)
                    intent.putExtra("songId",R.raw.aotmusic)
                }
                "ironman3theme" ->{
                    intent.putExtra("songTitle",songData.title)
                    intent.putExtra("songId",R.raw.ironman3theme)
                }
            }
            startActivity(intent)
        }
    }
}