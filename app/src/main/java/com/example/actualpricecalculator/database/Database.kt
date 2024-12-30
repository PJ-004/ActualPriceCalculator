package com.example.actualpricecalculator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SalesTaxDB.db"
        private const val DATABASE_VERSION = 1
    }

    private var database: SQLiteDatabase? = null

    // Since the database already exists, it is necessary to copy it to the Kotlin app so that it can be accessed
    init {
        copyDatabase()
    }

    // onCreate and onUpgrade do not need to be implemented as we are using a preloaded database
    // These are required to be declared by SQLiteOpenHelper, otherwise it would throw an error
    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun copyDatabase() {
        val dbFile = context.getDatabasePath(DATABASE_NAME)

        if (!dbFile.exists()) {
            try {
                context.assets.open(DATABASE_NAME).use { inputStream ->
                    FileOutputStream(dbFile).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException("Error copying database", e)
            }
        }
    }

    fun openDatabase(): SQLiteDatabase {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        database = SQLiteDatabase.openDatabase(dbPath.path, null, SQLiteDatabase.OPEN_READWRITE)
        return database!!
    }

    override fun close() {
        database?.close()
        super.close()
    }
}