package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ProductsAddActivity : AppCompatActivity() {

    private val productList = mutableListOf<String>()
    private lateinit var adapter: ProductListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_products_add, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                saveProductListToFile()
                true
            }
            R.id.menu_restore -> {
                showRestoreConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_add)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

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
        productList.add(productUrl)
        saveProductList()
        saveProductInfoToSharedPreferences(productName, productUrl)
        updateListView()
    }

    private fun saveProductInfoToSharedPreferences(productName: String, productUrl: String) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val productInfo = "Nombre: $productName, URL: $productUrl"
        editor.putString(productUrl, productInfo)
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

    private fun showEditProductDialog(productUrl: String, position: Int) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productInfo = sharedPreferences.getString(productUrl, "")
        val productName = productInfo?.substringAfter("Nombre: ")?.substringBefore(",") ?: ""
        val productUrl = productInfo?.substringAfter("URL: ") ?: ""

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Producto")

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null)
        builder.setView(view)

        val inputName = view.findViewById<EditText>(R.id.editTextProductName)
        val inputUrl = view.findViewById<EditText>(R.id.editTextProductUrl)

        inputName.setText(productName)
        inputUrl.setText(productUrl)

        builder.setPositiveButton("Guardar") { _, _ ->
            val newProductName = inputName.text.toString().trim()
            val newProductUrl = inputUrl.text.toString().trim()
            if (newProductName.isNotEmpty() && newProductUrl.isNotEmpty()) {
                editProduct(position, newProductName, newProductUrl)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun editProduct(position: Int, newProductName: String, newProductUrl: String) {
        saveProductInfoToSharedPreferences(newProductName, newProductUrl)

        productList[position] = newProductUrl
        saveProductList()
        updateListView()

        Log.d("ProductURL", "Producto editado: $newProductName - URL: $newProductUrl")
    }

    private fun showDeleteProductDialog(productUrl: String, position: Int) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productInfo = sharedPreferences.getString(productUrl, "")
        val productName = productInfo?.substringAfter("Nombre: ")?.substringBefore(",") ?: ""

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

    private fun saveProductListToFile() {
        val externalDir = getExternalFilesDir(null)
        val file = File(externalDir, "productos.txt")
        val writer = FileWriter(file)

        for (productUrl in productList) {
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
            val productInfo = sharedPreferences.getString(productUrl, "")
            val productName = productInfo?.substringAfter("Nombre: ")?.substringBefore(",") ?: ""
            writer.write("$productName,$productUrl\n")
        }
        writer.flush()
        writer.close()
    }

    private fun loadProductListFromFile() {
        val externalDir = getExternalFilesDir(null)
        val file = File(externalDir, "productos.txt")

        if (file.exists()) {
            val reader = FileReader(file)
            val bufferedReader = BufferedReader(reader)

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                Log.d("LoadProductList", "Read line: $line")
                val parts = line.split(",")
                if (parts.size == 2) {
                    val productName = parts[0]
                    val productUrl = parts[1]
                    productList.add(productUrl)
                    saveProductInfoToSharedPreferences(productName, productUrl)
                    Log.d("LoadProductList", "Added product: $productName, $productUrl")
                    saveProductList()
                    updateListView()
                }
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
        } else {
            Log.d("LoadProductList", "File does not exist")
        }
        Log.d("LoadProductList", "Product list size: ${productList.size}")
    }

    private fun showRestoreConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Restaurar Copia de Productos")
        builder.setMessage("¿Realmente desea restaurar una copia de los productos? Esto reemplazará la lista actual de productos.")

        builder.setPositiveButton("Sí") { _, _ ->
            loadProductListFromFile()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
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
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
            val productInfo = sharedPreferences.getString(fullProductUrl, "")
            val productName = productInfo?.substringAfter("Nombre: ")?.substringBefore(",") ?: fullProductUrl

            textView.text = productName

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