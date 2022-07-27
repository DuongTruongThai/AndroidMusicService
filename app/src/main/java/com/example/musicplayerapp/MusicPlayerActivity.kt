package com.example.musicplayerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import com.example.musicplayerapp.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityMusicPlayerBinding
    lateinit var serviceIntent: Intent
    var serviceStarted = false
    var musicDuration = 0
    var musicCurrentPosition = 0
    lateinit var runnable: Runnable
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var songIntent = intent

        binding.tvMusicTitle.text = songIntent.getStringExtra("songTitle")

        MusicService.songToPlay = songIntent.getIntExtra("songId",0)
        serviceIntent = Intent(applicationContext, MusicService::class.java)
        serviceIntent.setAction("NoAction")

        binding.apply {
            btnPlay.setOnClickListener {
                if (!serviceStarted){
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                    serviceStarted = true
                    startService(serviceIntent)
                    registerReceiver(getMusicDuration, IntentFilter("sendMusicDuration"))
                }
                else{
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    serviceStarted = false
                    MusicService.player.pause()
                }

            }
        }
    }

    private val getMusicDuration: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                musicDuration = intent.getIntExtra("musicDuration", 0)
                updateSeekBar()
            }
        }
    }

    private fun updateSeekBar() {
        binding.seekBar.max = musicDuration
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed){
                    MusicService.player.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        registerReceiver(getMusicCurrentPosition, IntentFilter("sendCurrentPosition"))
    }

    private val getMusicCurrentPosition: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                musicCurrentPosition = intent.getIntExtra("currentPosition", 0)
                runnable = Runnable {
                    binding.seekBar.progress = musicCurrentPosition
                    handler.postDelayed(runnable, 1000)
                }
                handler.postDelayed(runnable, 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::runnable.isInitialized)
            handler.removeCallbacks(runnable)
        stopService(serviceIntent)
    }
}