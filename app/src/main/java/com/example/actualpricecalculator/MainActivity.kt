package com.example.actualpricecalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import kotlin.math.roundToInt
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import com.example.actualpricecalculator.database.DatabaseHelper


class MainActivity : AppCompatActivity() {
    private lateinit var button : Button
    private lateinit var price : TextView
    private lateinit var result : TextView
    private var isEmpty: Boolean = true
    private lateinit var cityList: List<String>

    private fun initializeDropdownItems(): List<String> {
        val dbHelper = DatabaseHelper(this)
        val database = dbHelper.openDatabase()
        var taxRate = 0.0
        val returnValue = mutableListOf<String>()
        returnValue.add("Select Location")

        val cursor = database.rawQuery("SELECT Location FROM SalesTaxRates", arrayOf())

        if (cursor.moveToFirst()) {

            while (true) {
                returnValue.add(cursor.getString(0))

                if (!cursor.moveToNext()) {
                    break
                }

                cursor.moveToNext()
            }

            println(returnValue)
        }

        cursor.close()
        dbHelper.close()

        return returnValue
    }

    @SuppressLint("SetTextI18n")
    private fun calculateSalePrice(currentPrice : Double, location : String) {
        val dbHelper = DatabaseHelper(this)
        val database = dbHelper.openDatabase()
        var taxRate = 0.0

        val cursor = database.rawQuery("SELECT Rate FROM SalesTaxRates WHERE Location = '$location' OR County = '$location' AND Type = 'City' ", arrayOf())
        Log.i("Location: ", location)

        if (cursor.moveToFirst()) {
            taxRate = cursor.getDouble(0)
            println(taxRate)
        }

        cursor.close()
        dbHelper.close()
        if (!isEmpty) {
            val salePrice = (((taxRate * currentPrice) + currentPrice) * 100).roundToInt() / 100.0
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

        cityList = initializeDropdownItems()
        val dropdown = findViewById<Spinner>(R.id.cityList)
        val adapter = ArrayAdapter(this, cityList.lastIndex + 1, cityList)
        dropdown.adapter = adapter



        if (price.text.isEmpty()) {
            isEmpty = false
        } else {
            price.requestFocus()
            isEmpty = true
        }

        button.setOnClickListener {
            calculateSalePrice(price.text.toString().trim().toDouble(), "Cupertino")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}