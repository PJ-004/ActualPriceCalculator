package com.example.actualpricecalculator

import android.os.Bundle
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
            val sellPrice : Double = 7.25 * currentPrice
            result.text = sellPrice.toString()
            println(sellPrice)
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
            isEmpty = true
        }

        button.setOnClickListener {
            calculateSalePrice(price.toString().toDouble())
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}