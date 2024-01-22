package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProductsAddActivity : AppCompatActivity() {

    private val productList = mutableListOf<String>()
    private lateinit var adapter: ProductListAdapter
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

        adapter = ProductListAdapter(this, productList)
        listView.adapter = adapter

        loadProductList()

        fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            showOptionsDialog(productList[position], position)
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
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putStringSet("productList", productList.toSet())
        editor.apply()
    }

    private fun loadProductList() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        productList.clear()
        productList.addAll(sharedPreferences.getStringSet("productList", emptySet()) ?: emptySet())

        updateListView()
    }

    private fun updateListView() {
        adapter.notifyDataSetChanged()
    }

    private fun showOptionsDialog(productName: String, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opciones de Producto")

        val options = arrayOf("Editar", "Eliminar")

        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditProductDialog(productName, position)
                1 -> showDeleteProductDialog(productName, position)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun showEditProductDialog(productName: String, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Producto")

        val input = EditText(this)
        input.setText(productName.replace(baseUrl, ""))
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val newProductName = input.text.toString().trim()
            if (newProductName.isNotEmpty()) {
                editProduct(position, newProductName)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun editProduct(position: Int, newProductName: String) {
        productList[position] = baseUrl + newProductName
        saveProductList()
        updateListView()

        Log.d("ProductURL", "URL editada: ${productList[position]}")
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

    inner class ProductListAdapter(
        private val context: Context,
        private val productList: List<String>
    ) : BaseAdapter() {

        override fun getCount(): Int {
            return productList.size
        }

        override fun getItem(position: Int): Any {
            return productList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.list_item_product, parent, false)

            val textView = rowView.findViewById(R.id.textViewProduct) as TextView
            val editButton = rowView.findViewById(R.id.buttonEdit) as ImageButton
            val deleteButton = rowView.findViewById(R.id.buttonDelete) as ImageButton

            val productName = productList[position].replace(baseUrl, "")
            textView.text = productName

            editButton.setOnClickListener {
                showEditProductDialog(productList[position], position)
            }

            deleteButton.setOnClickListener {
                showDeleteProductDialog(productList[position], position)
            }

            return rowView
        }
    }
}