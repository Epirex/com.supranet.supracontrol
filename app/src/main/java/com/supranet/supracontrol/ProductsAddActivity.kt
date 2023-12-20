package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProductsAddActivity : AppCompatActivity() {

    private val productList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_add)

        val fabAddProduct: ExtendedFloatingActionButton = findViewById(R.id.fabAddProduct)
        val listView: ListView = findViewById(R.id.listViewProducts)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productList)
        listView.adapter = adapter

        loadProductList()

        fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun showAddProductDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Producto")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Agregar") { _, _ ->
            val productName = input.text.toString().trim()
            if (productName.isNotEmpty()) {
                addProductToList(productName)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addProductToList(productName: String) {
        productList.add(productName)
        saveProductList()
        updateListView()
    }

    private fun saveProductList() {
        val sharedPreferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putStringSet("productList", productList.toSet())
        editor.apply()
    }

    private fun loadProductList() {
        val sharedPreferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        productList.clear()
        productList.addAll(sharedPreferences.getStringSet("productList", emptySet()) ?: emptySet())

        updateListView()
    }

    private fun updateListView() {
        adapter.notifyDataSetChanged()
    }
}
