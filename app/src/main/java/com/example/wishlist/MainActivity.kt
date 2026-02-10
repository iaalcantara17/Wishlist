package com.example.wishlist

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var wishlistRv: RecyclerView
    private lateinit var nameEt: EditText
    private lateinit var priceEt: EditText
    private lateinit var urlEt: EditText
    private lateinit var addBtn: Button

    private val items: MutableList<WishItem> = mutableListOf()
    private lateinit var adapter: WishlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        wishlistRv = findViewById(R.id.wishlistRv)
        nameEt = findViewById(R.id.nameEt)
        priceEt = findViewById(R.id.priceEt)
        urlEt = findViewById(R.id.urlEt)
        addBtn = findViewById(R.id.addBtn)

        adapter = WishlistAdapter(
            items,
            onItemClicked = { item -> openUrl(item.url) },
            onItemLongPressed = { position -> deleteItemAt(position) }
        )

        wishlistRv.adapter = adapter
        wishlistRv.layoutManager = LinearLayoutManager(this)

        addBtn.setOnClickListener {
            addItemFromInputs()
        }
    }

    private fun addItemFromInputs() {
        val name = nameEt.text.toString().trim()
        val priceRaw = priceEt.text.toString().trim()
        val urlRaw = urlEt.text.toString().trim()

        val price = priceRaw.toDoubleOrNull()
        if (name.isEmpty() || price == null || urlRaw.isEmpty()) {
            Toast.makeText(this, "Enter a name, valid price, and URL", Toast.LENGTH_SHORT).show()
            return
        }

        val item = WishItem(name = name, price = price, url = normalizeUrl(urlRaw))
        items.add(item)
        adapter.notifyItemInserted(items.size - 1)

        nameEt.text.clear()
        priceEt.text.clear()
        urlEt.text.clear()
    }

    private fun deleteItemAt(position: Int) {
        if (position < 0 || position >= items.size) return
        items.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun normalizeUrl(raw: String): String {
        val trimmed = raw.trim()
        return if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) trimmed else "https://$trimmed"
    }

    private fun openUrl(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            ContextCompat.startActivity(this, browserIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_LONG).show()
        }
    }
}
