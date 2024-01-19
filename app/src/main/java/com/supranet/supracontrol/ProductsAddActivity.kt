package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProductsAddActivity : AppCompatActivity() {

    private val productList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    // Base URL for products
    private val baseUrl = "http://poster.com.ar/"

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_add)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val fabAddProduct: ExtendedFloatingActionButton = findViewById(R.id.fabAddProduct)
        val listView: ListView = findViewById(R.id.listViewProducts)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productList)
        listView.adapter = adapter

        loadProductList()

        fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteProductDialog(productList[position], position)
            true
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
        val productUrl = baseUrl + productName
        productList.add(productUrl)
        saveProductList()
        updateListView()

        Log.d("ProductURL", "URL guardada: $productUrl")
    }
    private fun saveProductList() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putStringSet("productList", productList.toSet())
        editor.apply()
    }

    private fun loadProductList() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        productList.clear()
        productList.addAll(sharedPreferences.getStringSet("productList", emptySet()) ?: emptySet())

        updateListView()
    }

    private fun updateListView() {
        val listView: ListView = findViewById(R.id.listViewProducts)

        val displayList = productList.map { it.replace(baseUrl, "") }.toTypedArray()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun showDeleteProductDialog(productName: String, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Producto")
        builder.setMessage("¿Desea eliminar el producto '$productName'?")

        builder.setPositiveButton("Eliminar") { _, _ ->
            deleteProduct(position)
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun deleteProduct(position: Int) {
        productList.removeAt(position)
        saveProductList()
        updateListView()
    }
}