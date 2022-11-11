package com.example.fsmapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Database(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    companion object{
        private const val DB_NAME = "article.db"
        private const val DB_VERSION = 1

        private const val SEARCHES_TABLE_NAME = "mySearches"
        private const val SEARCHES_HISTORY_COL = "searches_hist"

        private const val ARTICLES_TABLE_NAME = "myArticles"
        private const val ARTICLES_HISTORY_COL = "articles_hist"

        private const val SEARCHES_QUERY = "CREATE TABLE $SEARCHES_TABLE_NAME($SEARCHES_HISTORY_COL TEXT)"
        private const val ARTICLES_QUERY = "CREATE TABLE $ARTICLES_TABLE_NAME($ARTICLES_HISTORY_COL TEXT)"

        private val tables = arrayOf(SEARCHES_TABLE_NAME, ARTICLES_TABLE_NAME)
        private val cols = arrayOf(SEARCHES_HISTORY_COL, ARTICLES_HISTORY_COL)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SEARCHES_QUERY)
        db.execSQL(ARTICLES_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, ov: Int, nv: Int) {
        db.execSQL("DROP TABLE IF EXISTS $SEARCHES_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ARTICLES_TABLE_NAME")
        onCreate(db)
    }

    fun readHistory(table: String): ArrayList<String> {
        val db = writableDatabase
        val i = tableIndex(table)

        val cursor = db.rawQuery("SELECT * FROM ${tables[i]}", null)
        val historyList = ArrayList<String>()
        if (cursor.moveToFirst()) {
            do {
                historyList.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return historyList
    }

    fun saveHistory(historyList: ArrayList<String>, table: String) {
        val db = writableDatabase
        var values: ContentValues
        val i = tableIndex(table)
        for (entry in historyList) {
            values = ContentValues()
            values.put(cols[i], entry)
            db.insert(tables[i], null, values)
        }
        db.close()
    }

    fun clearHistory(table: String) {
        val db = writableDatabase
        val i = tableIndex(table)
        db.delete(tables[i], null, null)
        db.close()
    }

    private fun tableIndex(table: String) : Int {
        return when (table) {
            "searches" -> 0
            "articles" -> 1
            else -> throw Exception("invalid history column name, smh")
        }
    }
}