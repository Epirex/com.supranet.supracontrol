package com.supranet.supracontrol

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class ProductsListActivity : AppCompatActivity(), View.OnClickListener {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedUrls: MutableSet<String>
    private var selectedScreen: Int = 0
    private lateinit var baseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        baseUrl = sharedPreferences.getString("url_base", "http://supranet.ar/") ?: "http://supranet.ar/"

        // Lottie time!
        val lottieAnimationViewPantalla1: LottieAnimationView = findViewById(R.id.lottieAnimationView)
        val lottieAnimationViewPantalla2: LottieAnimationView = findViewById(R.id.lottieAnimationView2)
        val lottieAnimationViewPantalla3: LottieAnimationView = findViewById(R.id.lottieAnimationView3)
        val lottieAnimationViewPantalla4: LottieAnimationView = findViewById(R.id.lottieAnimationView4)

        lottieAnimationViewPantalla1.setAnimation(R.raw.send)
        lottieAnimationViewPantalla2.setAnimation(R.raw.send)
        lottieAnimationViewPantalla3.setAnimation(R.raw.send)
        lottieAnimationViewPantalla4.setAnimation(R.raw.send)

        lottieAnimationViewPantalla1.visibility = View.GONE
        lottieAnimationViewPantalla2.visibility = View.GONE
        lottieAnimationViewPantalla3.visibility = View.GONE
        lottieAnimationViewPantalla4.visibility = View.GONE

        lottieAnimationViewPantalla1.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationViewPantalla1.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        lottieAnimationViewPantalla2.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationViewPantalla2.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        lottieAnimationViewPantalla3.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationViewPantalla3.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        lottieAnimationViewPantalla4.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationViewPantalla4.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val pantalla1: ImageButton = findViewById(R.id.pantalla1)
        val pantalla2: ImageButton = findViewById(R.id.pantalla2)
        val pantalla3: ImageButton = findViewById(R.id.pantalla3)
        val pantalla4: ImageButton = findViewById(R.id.pantalla4)

        pantalla1.setOnClickListener(this)
        pantalla2.setOnClickListener(this)
        pantalla3.setOnClickListener(this)
        pantalla4.setOnClickListener(this)

        sharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productList = sharedPreferences.getStringSet("productList", emptySet())?.toTypedArray()
            ?: emptyArray()

        selectedUrls = sharedPreferences.getStringSet("selectedUrls", emptySet())?.toMutableSet()
            ?: mutableSetOf()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, getProductDisplayNames(productList))

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.adapter = adapter

        for (i in 0 until adapter.count) {
            if (selectedUrls.contains(adapter.getItem(i))) {
                listView.setItemChecked(i, true)
            }
        }

        if (selectedScreen != 0 && selectedUrls.isNotEmpty()) {
            // Logica para enviar la URL
            for (url in selectedUrls) {
                enviarUrl(url, selectedScreen)
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position).toString()
            val (_, url) = getProductInfo(productList[position])
            selectedUrls.clear()

            if (selectedUrls.contains(url)) {
                selectedUrls.remove(url)
            } else {
                selectedUrls.add(url)
            }
            saveSelectedUrls()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(view: View) {
        val pantalla1: ImageButton = findViewById(R.id.pantalla1)
        val pantalla2: ImageButton = findViewById(R.id.pantalla2)
        val pantalla3: ImageButton = findViewById(R.id.pantalla3)
        val pantalla4: ImageButton = findViewById(R.id.pantalla4)

        val lottieAnimationViewPantalla1: LottieAnimationView = findViewById(R.id.lottieAnimationView)
        val lottieAnimationViewPantalla2: LottieAnimationView = findViewById(R.id.lottieAnimationView2)
        val lottieAnimationViewPantalla3: LottieAnimationView = findViewById(R.id.lottieAnimationView3)
        val lottieAnimationViewPantalla4: LottieAnimationView = findViewById(R.id.lottieAnimationView4)

        lottieAnimationViewPantalla1.setAnimation(R.raw.send)
        lottieAnimationViewPantalla2.setAnimation(R.raw.send)
        lottieAnimationViewPantalla3.setAnimation(R.raw.send)
        lottieAnimationViewPantalla4.setAnimation(R.raw.send)

        when (view.id) {
            R.id.pantalla1 -> {
                animateButton(pantalla1)
                lottieAnimationViewPantalla1.visibility = View.VISIBLE
                lottieAnimationViewPantalla1.playAnimation()
                enviarUrlsSeleccionadas(1)
            }
            R.id.pantalla2 -> {
                animateButton(pantalla2)
                lottieAnimationViewPantalla2.visibility = View.VISIBLE
                lottieAnimationViewPantalla2.playAnimation()
                enviarUrlsSeleccionadas(2)
            }
            R.id.pantalla3 -> {
                animateButton(pantalla3)
                lottieAnimationViewPantalla3.visibility = View.VISIBLE
                lottieAnimationViewPantalla3.playAnimation()
                enviarUrlsSeleccionadas(3)
            }
            R.id.pantalla4 -> {
                animateButton(pantalla4)
                lottieAnimationViewPantalla4.visibility = View.VISIBLE
                lottieAnimationViewPantalla4.playAnimation()
                enviarUrlsSeleccionadas(4)
            }
        }
    }

    private fun animateButton(button: ImageButton) {
        val scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f)

        val scaleUpX = ObjectAnimator.ofFloat(button, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 1f)

        scaleDownX.duration = 150
        scaleDownY.duration = 150

        scaleUpX.duration = 150
        scaleUpY.duration = 150

        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)

        scaleDown.start()

        scaleDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                scaleUp.start()
            }
        })
    }

    private fun enviarUrlsSeleccionadas(pantalla: Int) {
        for (url in selectedUrls) {
            val fullUrl = baseUrl + url
            enviarUrl(fullUrl, pantalla)
            Log.d("ProductList", "URL: $fullUrl")
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
    private fun getProductDisplayNames(productList: Array<String>): Array<String> {
        return productList.map { getProductInfo(it).first }.toTypedArray()
    }

    private fun getProductInfo(productName: String): Pair<String, String> {
        val sharedPreferences = getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE)
        val productInfo = sharedPreferences.getString(productName, "") ?: ""
        val productName = productInfo.substringAfter("Nombre: ").substringBefore(",")
        val productUrl = productInfo.substringAfter("URL: ")
        return Pair(productName, productUrl)
    }
}