package com.example.actualpricecalculator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.OutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SalesTaxDB.db"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_PATH = "app/src/main/assets/SalesTaxDB.db"
    }

    private val dbPath = DATABASE_PATH + DATABASE_NAME
    private var database: SQLiteDatabase? = null
    private val appContext = context

    init {
        checkAndCopyDatabase()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Not used as we are using an existing database
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Not used as we are using an existing database
    }

    private fun checkAndCopyDatabase() {
        val dbFile = appContext.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase()
        }
    }

    private fun copyDatabase() {
        val inputStream = appContext.assets.open(DATABASE_NAME)
        val outputStream: OutputStream = FileOutputStream(dbPath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    fun openDatabase(): SQLiteDatabase {
        if (database == null || !database!!.isOpen) {
            database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE)
        }
        return database!!
    }

    @Synchronized
    override fun close() {
        database?.close()
        super.close()
    }
}