package com.supranet.supracontrol

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    private val ipAddress = "192.168.100.44" // Reemplaza con la dirección IP del dispositivo de transmisión
    private val port = 1234 // Puerto para la conexión
    private lateinit var socket: Socket
    private lateinit var outputStream: DataOutputStream
    private var isSocketInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Establecer la conexión de red en un hilo separado para evitar bloquear el hilo principal
        iniciarConexion()

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
                enviarUrl("http://azxtv.com/hls/stream.m3u8?PlaylistM3UCL")
                playSound()
            }
            R.id.button2 ->{
                enviarUrl("https://59f1cbe63db89.streamlock.net:1443/retroplustv/_definst_/retroplustv/playlist.m3u8?PlaylistM3UCL")
                playSound()
            }
            R.id.button3 ->{
                enviarUrl("https://live-01-02-eltrece.vodgc.net/eltrecetv/index.m3u8?PlaylistM3UCL")
                playSound()
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
        if (isSocketInitialized && socket.isConnected) {
            Thread {
                try {
                    outputStream.writeUTF(url)
                    outputStream.flush()
                    Log.d("ControlApp", "URL enviada: $url")
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("ControlApp", "Error al enviar la URL: ${e.message}")
                }
            }.start()
        } else {
            // Si la conexión no está establecida, intenta establecerla nuevamente
            iniciarConexion()
        }
    }

    private fun iniciarConexion() {
        Thread {
            try {
                socket = Socket(ipAddress, port)
                outputStream = DataOutputStream(socket.getOutputStream())

                isSocketInitialized = true

                Log.d("ControlApp", "Conexión de socket establecida")

            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("ControlApp", "Error al establecer la conexión de socket: ${e.message}")
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}