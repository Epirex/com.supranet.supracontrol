package com.supranet.supracontrol

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences

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
        val buttonscreens: Button = findViewById(R.id.buttonscreens)
        val buttonmenu: Button = findViewById(R.id.buttonmenu)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonscreens.setOnClickListener(this)
        buttonmenu.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val button1_url = sharedPreferences.getString("button1_url", "http://www.supranet.ar")
        val button2_url = sharedPreferences.getString("button2_url", "http://www.supranet.ar")
        val button3_url = sharedPreferences.getString("button3_url", "http://www.supranet.ar")
        val button4_url = sharedPreferences.getString("button4_url", "http://www.supranet.ar")
        val button5_url = sharedPreferences.getString("button5_url", "http://www.supranet.ar")
        val button6_url = sharedPreferences.getString("button6_url", "http://www.supranet.ar")
        val button7_url = sharedPreferences.getString("button7_url", "http://www.supranet.ar")
        val button8_url = sharedPreferences.getString("button8_url", "http://www.supranet.ar")
        val button9_url = sharedPreferences.getString("button9_url", "http://www.supranet.ar")
        when (view.id) {
            R.id.button1 ->{
                enviarUrl(button1_url.toString())
                //playSound()
            }
            R.id.button2 ->{
                enviarUrl(button2_url.toString())
                //playSound()
            }
            R.id.button3 ->{
                enviarUrl(button3_url.toString())
                //playSound()
            }
            R.id.button4 ->{
                enviarUrl(button4_url.toString())
                //playSound()
            }
            R.id.button5 ->{
                enviarUrl(button5_url.toString())
                //playSound()
            }
            R.id.button6 ->{
                enviarUrl(button6_url.toString())
                //playSound()
            }
            R.id.button7 ->{
                enviarUrl(button7_url.toString())
                //playSound()
            }
            R.id.button8 ->{
                enviarUrl(button8_url.toString())
                //playSound()
            }
            R.id.button9 ->{
                enviarUrl(button9_url.toString())
                //playSound()
            }
            R.id.buttonscreens ->{
                val intent = Intent(this, ScreensActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonmenu ->{
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
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
                val sharedPreferences = getSharedPreferences("IP_PREFERENCES", Context.MODE_PRIVATE)
                val ipAddressSet = sharedPreferences.getStringSet("IP_ADDRESSES", HashSet()) ?: HashSet()

                for (ipAddress in ipAddressSet) {
                    try {
                        val socket = Socket(ipAddress, 12345)
                        val output = PrintWriter(socket.getOutputStream(), true)
                        output.println(url)
                        socket.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
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