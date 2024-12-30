package com.example.actualpricecalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.DeadObjectException
import android.util.Log
import android.view.View
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
import java.io.IOException
import java.util.concurrent.CompletionException

class MainActivity : AppCompatActivity() {
    private lateinit var button : Button
    private lateinit var price : TextView
    private lateinit var result : TextView
    private lateinit var cityList: List<String>

    private var isEmpty: Boolean = true
    private var currentCity: String = "Select Location"

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
        Log.i("Location: ", location)       // This log helps verify that the correct location has been selected

        if (cursor.moveToFirst()) {
            taxRate = cursor.getDouble(0)
            println(taxRate)
        }

        cursor.close()
        dbHelper.close()
        if (!isEmpty) {
            // The division by 100 is done to return the value at 2 decimal points
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

        val dropdown = findViewById<Spinner>(R.id.cityDropdown)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown.adapter = adapter

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentCity = cityList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        if (price.text.isEmpty()) {
            isEmpty = false
        } else {
            price.requestFocus()
            isEmpty = true
        }

        button.setOnClickListener {
            try {
                if (!isEmpty) {
                    calculateSalePrice(price.text.toString().trim().toDouble(), currentCity)
                }
            } catch (e: NumberFormatException) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Please enter a valid value for current price")
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}