package com.example.actualpricecalculator

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.roundToInt
import com.example.actualpricecalculator.database.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var button : Button
    private lateinit var price : TextView
    private lateinit var result : TextView
    private var isEmpty: Boolean = true

    //var taxRates = DatabaseHelper(this)

    fun calculateSalePrice(currentPrice : Double) {
        if (!isEmpty) {
            val salePrice = (((0.09125 * currentPrice) + currentPrice) * 100).roundToInt() / 100.0
            result.text = salePrice.toString()
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
            calculateSalePrice(price.text.toString().trim().toDouble())
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}