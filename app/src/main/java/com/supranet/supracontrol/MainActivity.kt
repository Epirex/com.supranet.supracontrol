package com.supranet.supracontrol

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button1 ->{
                enviarUrl("http://poster.com.ar/marito/")
                //playSound()
            }
            R.id.button2 ->{
                enviarUrl("http://poster.com.ar/sanroque/")
                //playSound()
            }
            R.id.button3 ->{
                enviarUrl("https://live-01-02-eltrece.vodgc.net/eltrecetv/index.m3u8?PlaylistM3UCL")
                //playSound()
            }
            R.id.button4 -> playSound()
            R.id.button5 -> playSound()
            R.id.button6 -> playSound()
            R.id.button7 -> playSound()
            R.id.button8 -> playSound()
            R.id.button9 -> playSound()
        }
    }

    private fun playSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(0)
        } else {
            mediaPlayer.start()
        }
    }

    private fun enviarUrl(url: String) {
        Thread {
            try {
                val socket = Socket("192.168.100.32", 12345)
                val output = PrintWriter(socket.getOutputStream(), true)
                output.println(url)
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}