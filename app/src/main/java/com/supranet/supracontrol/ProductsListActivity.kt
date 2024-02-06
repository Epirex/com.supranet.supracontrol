package com.supranet.supracontrol

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class ProductsListActivity : AppCompatActivity(), View.OnClickListener {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedUrls: MutableSet<String>
    private var selectedScreen: Int = 0
    private val baseUrl = "http://supranet.ar/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val pantalla1: ImageButton = findViewById(R.id.pantalla1)
        val pantalla2: ImageButton = findViewById(R.id.pantalla2)
        val pantalla3: ImageButton = findViewById(R.id.pantalla3)
        val pantalla4: ImageButton = findViewById(R.id.pantalla4)

        pantalla1.setOnClickListener(this)
        pantalla2.setOnClickListener(this)
        pantalla3.setOnClickListener(this)
        pantalla4.setOnClickListener(this)

        sharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productList =
            sharedPreferences.getStringSet("productList", emptySet())?.toTypedArray()
                ?: emptyArray()

        selectedUrls =
            sharedPreferences.getStringSet("selectedUrls", emptySet())?.toMutableSet()
                ?: mutableSetOf()

        // Limpiar la ultima URL seleccionada
        selectedUrls.clear()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, getProductDisplayNames(productList))

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.adapter = adapter

        for (i in 0 until adapter.count) {
            if (selectedUrls.contains(adapter.getItem(i))) {
                listView.setItemChecked(i, true)
            }
        }

        if (selectedScreen != 0 && selectedUrls.isNotEmpty()) {
            // Logica para enviar la URL
            for (url in selectedUrls) {
                enviarUrl(url, selectedScreen)
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position).toString()

            selectedUrls.clear()

            if (selectedUrls.contains(item)) {
                selectedUrls.remove(item)
            } else {
                selectedUrls.add(item)
            }
            saveSelectedUrls()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(view: View) {
        val pantalla1: ImageButton = findViewById(R.id.pantalla1)
        val pantalla2: ImageButton = findViewById(R.id.pantalla2)
        val pantalla3: ImageButton = findViewById(R.id.pantalla3)
        val pantalla4: ImageButton = findViewById(R.id.pantalla4)
        when (view.id) {
            R.id.pantalla1 -> {
                animateButton(pantalla1)
                enviarUrlsSeleccionadas(1)
            }
            R.id.pantalla2 -> {
                animateButton(pantalla2)
                enviarUrlsSeleccionadas(2)
            }
            R.id.pantalla3 -> {
                animateButton(pantalla3)
                enviarUrlsSeleccionadas(3)
            }
            R.id.pantalla4 -> {
                animateButton(pantalla4)
                enviarUrlsSeleccionadas(4)
            }
        }
    }

    private fun animateButton(button: ImageButton) {
        val scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f)

        val scaleUpX = ObjectAnimator.ofFloat(button, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 1f)

        scaleDownX.duration = 150
        scaleDownY.duration = 150

        scaleUpX.duration = 150
        scaleUpY.duration = 150

        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)

        scaleDown.start()

        scaleDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                scaleUp.start()
            }
        })
    }

    private fun enviarUrlsSeleccionadas(pantalla: Int) {
        for (url in selectedUrls) {
            val fullUrl = baseUrl + url
            enviarUrl(fullUrl, pantalla)
        }
    }

    private fun saveSelectedUrls() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedUrls", selectedUrls)
        editor.apply()
    }

    private fun enviarUrl(url: String, pantalla: Int) {
        sharedPreferences = getSharedPreferences("IP_PREFERENCES", Context.MODE_PRIVATE)
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
    private fun getProductDisplayNames(productList: Array<String>): Array<String> {
        return productList.map { getProductName(it) }.toTypedArray()
    }

    private fun getProductName(fullUrl: String): String {
        return fullUrl.replace(baseUrl, "")
    }
}