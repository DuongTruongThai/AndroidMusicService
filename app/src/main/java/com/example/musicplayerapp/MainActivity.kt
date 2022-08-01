package com.example.musicplayerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val listData: MutableList<SongData> = mutableListOf()
    private var songClicked: ((SongData) -> Unit)? = null
    private val adapter: MyAdapter? by lazy { songClicked?.let { MyAdapter(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addSongList()
        songClicked = { songData ->
            val intent = Intent(this, MusicPlayerActivity::class.java)
            when (songData.title) {
                songTitle.AOTMUSIC.songName -> {
                    intent.putExtra("songTitle", songData.title)
                    intent.putExtra("songId", R.raw.aotmusic)
                }
                songTitle.IRONMANTHEME.songName -> {
                    intent.putExtra("songTitle", songData.title)
                    intent.putExtra("songId", R.raw.ironman3theme)
                }
            }
            startActivity(intent)
        }
        adapter?.setData(listData)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun addSongList(){
        var fields = R.raw::class.java.getFields()
        fields.forEach { field ->
            listData.add(SongData(field.name))
        }
    }

    enum class songTitle(val songName: String){
        AOTMUSIC("aotmusic"),
        IRONMANTHEME("ironman3theme")
    }
}