package com.supranet.supracontrol

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val screenItem = menu?.findItem(R.id.screenitem)
        screenItem?.setOnMenuItemClickListener {
            showPasswordDialog("screen")
            true
        }
        val menuItem = menu?.findItem(R.id.menuitem)
        menuItem?.setOnMenuItemClickListener {
            showPasswordDialog("menu")
            true
        }
        val addItem = menu?.findItem(R.id.additem)
        addItem?.setOnMenuItemClickListener {
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

        // Lottie time!
        lottieAnimationView = findViewById(R.id.lottieAnimationView)
        lottieAnimationView.visibility = View.GONE
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationView.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        sharedPreferences = getSharedPreferences("IP_PREFERENCES", Context.MODE_PRIVATE)

        val button1: ImageButton = findViewById(R.id.button1)
        val button2: ImageButton = findViewById(R.id.button2)
        val button3: ImageButton = findViewById(R.id.button3)
        val button4: ImageButton = findViewById(R.id.button4)
        val button5: ImageButton = findViewById(R.id.button5)
        val button6: ImageButton = findViewById(R.id.button6)
        val pantalla1: ImageButton = findViewById(R.id.pantalla1)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        pantalla1.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val button1urls = sharedPreferences.getString("button1_url", "http://www.supranet.ar")
            ?.split(", ")
        val button2urls = sharedPreferences.getString("button2_url", "http://www.supranet.ar")
            ?.split(", ")
        val button3urls = sharedPreferences.getString("button3_url", "http://www.supranet.ar")
            ?.split(", ")
        val button4urls = sharedPreferences.getString("button4_url", "http://www.supranet.ar")
            ?.split(", ")
        val button5urls = sharedPreferences.getString("button5_url", "http://www.supranet.ar")
            ?.split(", ")
        val button6urls = sharedPreferences.getString("button6_url", "http://www.supranet.ar")
            ?.split(", ")
        when (view.id) {
            R.id.button1 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button1urls != null) {
                    for ((index, url) in button1urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button2 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button2urls != null) {
                    for ((index, url) in button2urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button3 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button3urls != null) {
                    for ((index, url) in button3urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button4 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button4urls != null) {
                    for ((index, url) in button4urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button5 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button5urls != null) {
                    for ((index, url) in button5urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.button6 -> {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                if (button6urls != null) {
                    for ((index, url) in button6urls.withIndex()) {
                        enviarUrl(url, index + 1)
                    }
                }
            }
            R.id.pantalla1 -> {
                val intent = Intent(this, ProductsListActivity::class.java)
                startActivity(intent)
            }
        }
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

    private fun showPasswordDialog(action: String) {
        val passwordDialog = Dialog(this)
        passwordDialog.setContentView(R.layout.password)
        passwordDialog.setCancelable(false)

        val buttonpass1 = passwordDialog.findViewById<Button>(R.id.buttonpass1)
        val buttonpass2 = passwordDialog.findViewById<Button>(R.id.buttonpass2)
        val buttonpass3 = passwordDialog.findViewById<Button>(R.id.buttonpass3)
        val buttonpass4 = passwordDialog.findViewById<Button>(R.id.buttonpass4)
        val buttonpass5 = passwordDialog.findViewById<Button>(R.id.buttonpass5)
        val buttonpass6 = passwordDialog.findViewById<Button>(R.id.buttonpass6)
        val buttonpass7 = passwordDialog.findViewById<Button>(R.id.buttonpass7)
        val buttonpass8 = passwordDialog.findViewById<Button>(R.id.buttonpass8)
        val buttonpass9 = passwordDialog.findViewById<Button>(R.id.buttonpass9)
        val buttonpass0 = passwordDialog.findViewById<Button>(R.id.buttonpass0)
        val buttonClear = passwordDialog.findViewById<Button>(R.id.buttonClear)
        val buttonExit = passwordDialog.findViewById<Button>(R.id.buttonExit)
        val sendButton = passwordDialog.findViewById<Button>(R.id.buttonDone)
        val passwordEditText = passwordDialog.findViewById<EditText>(R.id.passwordEditText)

        buttonpass1.setOnClickListener {
            passwordEditText.append("1")
        }
        buttonpass2.setOnClickListener {
            passwordEditText.append("2")
        }
        buttonpass3.setOnClickListener {
            passwordEditText.append("3")
        }
        buttonpass4.setOnClickListener {
            passwordEditText.append("4")
        }
        buttonpass5.setOnClickListener {
            passwordEditText.append("5")
        }
        buttonpass6.setOnClickListener {
            passwordEditText.append("6")
        }
        buttonpass7.setOnClickListener {
            passwordEditText.append("7")
        }
        buttonpass8.setOnClickListener {
            passwordEditText.append("8")
        }

        buttonpass9.setOnClickListener {
            passwordEditText.append("9")
        }

        buttonpass0.setOnClickListener {
            passwordEditText.append("0")
        }
        buttonClear.setOnClickListener {
            val text = passwordEditText.text
            if (text.isNotEmpty()) {
                passwordEditText.text.delete(text.length - 1, text.length)
            }
        }
        buttonExit.setOnClickListener {
            passwordEditText.text.clear()
            passwordDialog.dismiss()
        }
        sendButton.setOnClickListener {
            checkPassword(passwordEditText.text.toString(), action)
            passwordEditText.text.clear()
            passwordDialog.dismiss()
        }

        passwordDialog.show()
    }

    private fun checkPassword(password: String, action: String) {
        if (password == "3434") {
            when (action) {
                "menu" -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                }
                "screen" -> {
                    val intent = Intent(this, ScreensActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(this, "¡Contraseña incorrecta!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}