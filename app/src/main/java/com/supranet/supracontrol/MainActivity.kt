package com.supranet.supracontrol

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private var floatingDialog: AlertDialog? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val ScreenItem = menu?.findItem(R.id.screenitem)
        ScreenItem?.setOnMenuItemClickListener {
            val intent = Intent(this, ScreensActivity::class.java)
            startActivity(intent)
            true
        }
        val MenuItem = menu?.findItem(R.id.menuitem)
        MenuItem?.setOnMenuItemClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            true
        }
        val AddItem = menu?.findItem(R.id.additem)
        AddItem?.setOnMenuItemClickListener {
            val intent = Intent(this, ProductsAddActivity::class.java)
            startActivity(intent)
            true
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        sharedPreferences = getSharedPreferences("IP_PREFERENCES", Context.MODE_PRIVATE)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val pantalla1: Button = findViewById(R.id.pantalla1)
        val pantalla2: Button = findViewById(R.id.pantalla2)
        val pantalla3: Button = findViewById(R.id.pantalla3)
        val pantalla4: Button = findViewById(R.id.pantalla4)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        pantalla1.setOnClickListener(this)
        pantalla2.setOnClickListener(this)
        pantalla3.setOnClickListener(this)
        pantalla4.setOnClickListener(this)

        button1.setOnLongClickListener  {showFloatingDialog()}
        button2.setOnLongClickListener  {showFloatingDialog()}
        button3.setOnLongClickListener  {showFloatingDialog()}
        button4.setOnLongClickListener  {showFloatingDialog()}
        button5.setOnLongClickListener  {showFloatingDialog()}
        button6.setOnLongClickListener  {showFloatingDialog()}
        button7.setOnLongClickListener  {showFloatingDialog()}
        button8.setOnLongClickListener  {showFloatingDialog()}
        button9.setOnLongClickListener  {showFloatingDialog()}
        pantalla1.setOnLongClickListener  {showFloatingDialog()}
        pantalla2.setOnLongClickListener  {showFloatingDialog()}
        pantalla3.setOnLongClickListener  {showFloatingDialog()}
        pantalla4.setOnLongClickListener  {showFloatingDialog()}
    }

    override fun onClick(view: View) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val button1_urls = sharedPreferences.getString("button1_url", "http://www.supranet.ar")
            ?.split(", ")
        val button2_urls = sharedPreferences.getString("button2_url", "http://www.supranet.ar")
            ?.split(", ")
        val button3_urls = sharedPreferences.getString("button3_url", "http://www.supranet.ar")
            ?.split(", ")
        val button4_urls = sharedPreferences.getString("button4_url", "http://www.supranet.ar")
            ?.split(", ")
        val button5_urls = sharedPreferences.getString("button5_url", "http://www.supranet.ar")
            ?.split(", ")
        val button6_urls = sharedPreferences.getString("button6_url", "http://www.supranet.ar")
            ?.split(", ")
        val button7_urls = sharedPreferences.getString("button7_url", "http://www.supranet.ar")
            ?.split(", ")
        val button8_urls = sharedPreferences.getString("button8_url", "http://www.supranet.ar")
            ?.split(", ")
        val button9_urls = sharedPreferences.getString("button9_url", "http://www.supranet.ar")
            ?.split(", ")
        when (view.id) {
            R.id.button1 -> {
                if (button1_urls != null) {
                    for ((index, url) in button1_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button2 -> {
                if (button2_urls != null) {
                    for ((index, url) in button2_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button3 -> {
                if (button3_urls != null) {
                    for ((index, url) in button3_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button4 -> {
                if (button4_urls != null) {
                    for ((index, url) in button4_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button5 -> {
                if (button5_urls != null) {
                    for ((index, url) in button5_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button6 -> {
                if (button6_urls != null) {
                    for ((index, url) in button6_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button7 -> {
                if (button7_urls != null) {
                    for ((index, url) in button7_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button8 -> {
                if (button8_urls != null) {
                    for ((index, url) in button8_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button9 -> {
                if (button9_urls != null) {
                    for ((index, url) in button9_urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.pantalla1 -> {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.pantalla2 -> {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.pantalla3 -> {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.pantalla4 -> {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showFloatingDialog(): Boolean {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.products, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("Salir") { dialog, _ -> dialog.dismiss() }

        floatingDialog?.dismiss()
        floatingDialog = builder.create()
        floatingDialog?.show()

        return true
    }

    private fun enviarUrl(url: String, pantalla: Int) {
        Thread {
            val key = "screen$pantalla" + "_ip"
            val ipAddress = sharedPreferences.getString(key, "")
            if (!ipAddress.isNullOrEmpty()) {
                try {
                    val socket = Socket(ipAddress, 12345)
                    val output = PrintWriter(socket.getOutputStream(), true)
                    output.println(url)
                    socket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}