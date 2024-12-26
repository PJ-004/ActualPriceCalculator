package com.example.actualpricecalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var button : Button
    private lateinit var price : TextView
    private lateinit var result : TextView
    private var isEmpty: Boolean = true

    fun calculateSalePrice(currentPrice : Double) {
        if (!isEmpty) {
            val salePrice : Double = 7.25 * currentPrice
            result.text = salePrice.toString()
            println(salePrice)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        price = findViewById(R.id.price)
        button = findViewById(R.id.button)
        result = findViewById(R.id.result)

        if (!price.text.isEmpty()) {
            price.requestFocus()
            isEmpty = true
        } else {
            isEmpty = false
        }

        button.setOnClickListener {
            if (isEmpty) {
                Log.i("You're", "fucked")
            }
            price.requestFocus()
            Log.i("The current price is, ", price.text.toString())
            calculateSalePrice(price.text.toString().trim().toDouble())
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}