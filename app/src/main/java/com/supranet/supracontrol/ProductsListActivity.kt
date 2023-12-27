package com.supranet.supracontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ProductsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        val opciones = arrayOf("Producto 1", "Producto 2", "Producto 3", "Producto 4", "Producto 5")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, opciones)

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = listView.getItemAtPosition(position).toString()
        }
    }
}