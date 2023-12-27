package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ProductsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        val sharedPreferences: SharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productList = sharedPreferences.getStringSet("productList", emptySet())?.toTypedArray() ?: emptyArray()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, productList)

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = listView.getItemAtPosition(position).toString()
        }
    }
}