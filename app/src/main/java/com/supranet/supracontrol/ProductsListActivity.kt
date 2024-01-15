package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ProductsListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedUrls: MutableSet<String>

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

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position).toString()
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
}