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

    private val PRODUCT_NAME_PREF = "productNamePref"
    private val PRODUCT_URL_PREF = "productUrlPref"

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

        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(view)

        val inputName = view.findViewById<EditText>(R.id.editTextProductName)
        val inputUrl = view.findViewById<EditText>(R.id.editTextProductUrl)

        builder.setPositiveButton("Agregar") { _, _ ->
            val productName = inputName.text.toString().trim()
            val productUrl = inputUrl.text.toString().trim()

            if (productName.isNotEmpty() && productUrl.isNotEmpty()) {
                addProductToList(productName, productUrl)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addProductToList(productName: String, productUrl: String) {
        val fullUrl = baseUrl + productUrl
        productList.add(fullUrl)
        saveProductList()
        saveProductInfoToSharedPreferences(productName, fullUrl)
        updateListView()
    }

    private fun saveProductInfoToSharedPreferences(productName: String, productUrl: String) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString("$PRODUCT_NAME_PREF$productUrl", productName)
        editor.putString("$PRODUCT_URL_PREF$productUrl", productUrl)
        editor.apply()
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

            val fullProductUrl = productList[position]
            val productName = fullProductUrl.replace(baseUrl, "")

            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
            val storedProductName = sharedPreferences.getString("$PRODUCT_NAME_PREF$fullProductUrl", "")

            textView.text = if (storedProductName.isNullOrEmpty()) productName else storedProductName

            editButton.setOnClickListener {
                showEditProductDialog(fullProductUrl, position)
            }

            deleteButton.setOnClickListener {
                showDeleteProductDialog(fullProductUrl, position)
            }

            return rowView
        }
    }
}