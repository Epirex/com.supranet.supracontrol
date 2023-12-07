package com.supranet.supracontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class ScreensActivity : AppCompatActivity() {

    private lateinit var ipCardContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screens)

        val extendedFab: ExtendedFloatingActionButton = findViewById(R.id.extended_fab)
        ipCardContainer = findViewById(R.id.ip_card_container)

        sharedPreferences = getSharedPreferences("IP_PREFERENCES", Context.MODE_PRIVATE)

        // Cargar direcciones IP almacenadas
        loadSavedIpAddresses()

        extendedFab.setOnClickListener {
            showIpInputDialog()
        }
    }

    private fun showIpInputDialog() {
        val inputLayout = TextInputLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        inputLayout.layoutParams = layoutParams

        val ipAddressEditText = EditText(this)
        ipAddressEditText.hint = "Introduce la dirección IP"
        inputLayout.addView(ipAddressEditText)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Configurar IP")
            .setView(inputLayout)
            .setPositiveButton("Guardar") { _, _ ->
                saveIpAddress(ipAddressEditText.text.toString())
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun saveIpAddress(ipAddress: String) {
        val editor = sharedPreferences.edit()
        val ipAddressSet = HashSet(sharedPreferences.getStringSet("IP_ADDRESSES", HashSet())!!)
        ipAddressSet.add(ipAddress)
        editor.putStringSet("IP_ADDRESSES", ipAddressSet)
        editor.apply()

        // Crear una nueva tarjeta para la dirección IP
        val newCard = createIpCard(ipAddress)

        // Agregar la nueva tarjeta al contenedor
        ipCardContainer.addView(newCard)
    }

    private fun loadSavedIpAddresses() {
        val ipAddressSet = sharedPreferences.getStringSet("IP_ADDRESSES", HashSet()) ?: HashSet()
        for (ipAddress in ipAddressSet) {
            val newCard = createIpCard(ipAddress)
            ipCardContainer.addView(newCard)
        }
    }

    private fun createIpCard(ipAddress: String): CardView {
        val cardView = CardView(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.card_margin)
        cardView.layoutParams = layoutParams
        cardView.radius = resources.getDimensionPixelSize(R.dimen.card_corner_radius).toFloat()
        cardView.cardElevation = resources.getDimensionPixelSize(R.dimen.card_elevation).toFloat()

        val textView = TextView(this)
        textView.text = "Dirección IP: $ipAddress"
        val padding = resources.getDimensionPixelSize(R.dimen.card_padding)
        textView.setPadding(padding, padding, padding, padding)

        cardView.addView(textView)
        return cardView
    }
}