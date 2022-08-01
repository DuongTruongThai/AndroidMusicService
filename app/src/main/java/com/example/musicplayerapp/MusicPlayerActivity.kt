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
import java.util.*

class MusicPlayerActivity : AppCompatActivity() {

    private val binding: ActivityMusicPlayerBinding by lazy { ActivityMusicPlayerBinding.inflate(layoutInflater) }
    lateinit var serviceIntent: Intent
    var serviceStarted = false
    var musicDuration = 0
    var musicCurrentPosition = 0
    private val timer = Timer()
    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var songIntent = intent

        binding.tvMusicTitle.text = songIntent.getStringExtra("songTitle")

        MusicService.songToPlay = songIntent.getIntExtra("songId", 0)
        serviceIntent = Intent(applicationContext, MusicService::class.java)
        serviceIntent.setAction("NoAction")

        binding.apply {
            btnPlay.setOnClickListener {
                if (!serviceStarted) {
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                    serviceStarted = true
                    startService(serviceIntent)
                    registerReceiver(musicInfoReceiver, IntentFilter("sendMusicDuration"))
                } else {
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    serviceStarted = false
                    MusicService.player.pause()
                }

            }
        }
    }

    private val musicInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isReceiverRegistered = true
            val intentAction = intent?.action.toString()
            if (intentAction.equals("sendMusicDuration")) {
                if (intent != null) {
                    musicDuration = intent.getIntExtra("musicDuration", 0)
                }
                updateSeekBar()
            } else if (intentAction.equals("sendCurrentPosition")) {
                if (intent != null) {
                    musicCurrentPosition = intent.getIntExtra("currentPosition", 0)
                    timer.scheduleAtFixedRate(UpdateSeekBarProgress(), 0, 500)
                }
            }
        }
    }

    private fun updateSeekBar() {
        binding.seekBar.max = musicDuration
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    MusicService.player.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        registerReceiver(musicInfoReceiver, IntentFilter("sendCurrentPosition"))
    }

    private inner class UpdateSeekBarProgress() : TimerTask() {
        override fun run() {
            binding.seekBar.progress = musicCurrentPosition
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timer.purge()
        if (isReceiverRegistered){
            unregisterReceiver(musicInfoReceiver)
        }
        stopService(serviceIntent)
    }
}