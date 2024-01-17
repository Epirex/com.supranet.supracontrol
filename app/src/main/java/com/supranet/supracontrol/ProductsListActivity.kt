package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class ProductsListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedUrls: MutableSet<String>
    private var selectedScreen: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        sharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productList = sharedPreferences.getStringSet("productList", emptySet())?.toTypedArray() ?: emptyArray()

        selectedUrls = sharedPreferences.getStringSet("selectedUrls", emptySet())?.toMutableSet() ?: mutableSetOf()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, productList)

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.adapter = adapter

        for (i in 0 until adapter.count) {
            if (selectedUrls.contains(adapter.getItem(i))) {
                listView.setItemChecked(i, true)
            }
        }

        val toggleButton: MaterialButtonToggleGroup = findViewById(R.id.toggleButton)
        toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                selectedScreen = group.indexOfChild(group.findViewById(checkedId)) + 1
            }
        }

        val enviarButton: Button = findViewById(R.id.enviarButton)
        enviarButton.setOnClickListener {
            selectedUrls.clear()
            for (i in 0 until adapter.count) {
                if (listView.isItemChecked(i)) {
                    selectedUrls.add(adapter.getItem(i).toString())
                }
            }

            if (selectedScreen != 0 && selectedUrls.isNotEmpty()) {
                // Enviar la URL seleccionada a la IP correspondiente
                for (url in selectedUrls) {
                    enviarUrl(url, selectedScreen)
                }
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position).toString()

            // Desmarcar todos los elementos anteriores
            for (i in 0 until adapter.count) {
                if (i != position) {
                    listView.setItemChecked(i, false)
                }
            }

            if (selectedUrls.contains(item)) {
                selectedUrls.remove(item)
            } else {
                selectedUrls.add(item)
            }
            saveSelectedUrls()
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
}