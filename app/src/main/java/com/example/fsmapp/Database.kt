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
        private const val ARTICLES_DATE_COL = "articles_date"
        private const val ARTICLES_SOURCE_COL = "articles_source"
        private const val ARTICLES_TITLE_COL = "articles_title"
        private const val ARTICLES_URL_COL = "articles_url"
        private const val ARTICLES_IMAGE_COL = "articles_image"

        private const val SEARCHES_QUERY = "CREATE TABLE $SEARCHES_TABLE_NAME($SEARCHES_HISTORY_COL TEXT)"
        private const val ARTICLES_QUERY = "CREATE TABLE $ARTICLES_TABLE_NAME($ARTICLES_DATE_COL TEXT,$ARTICLES_SOURCE_COL TEXT,$ARTICLES_TITLE_COL TEXT,$ARTICLES_URL_COL TEXT,$ARTICLES_IMAGE_COL TEXT)"

        private val tables = arrayOf(SEARCHES_TABLE_NAME, ARTICLES_TABLE_NAME)
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

    fun readSearches(): ArrayList<String> {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $SEARCHES_TABLE_NAME", null)
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

    fun readArticles(): ArrayList<NewsResult.Article> {
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT * FROM $ARTICLES_TABLE_NAME", null)
        val historyList = ArrayList<NewsResult.Article>()
        if (cursor.moveToFirst()) {
            do {
                historyList.add(NewsResult.Article("0","0","0",cursor.getString(0),NewsResult.Source("0",cursor.getString(1)),cursor.getString(2),cursor.getString(3),cursor.getString(4)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }

    fun addSearch(entry: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(SEARCHES_HISTORY_COL, entry)
        db.insert(SEARCHES_TABLE_NAME, null, values)
        db.close()
    }

    fun addArticle(article: NewsResult.Article) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(ARTICLES_DATE_COL, article.publishedAt)
        values.put(ARTICLES_SOURCE_COL, article.source.name)
        values.put(ARTICLES_TITLE_COL, article.title)
        values.put(ARTICLES_URL_COL, article.url)
        values.put(ARTICLES_IMAGE_COL, article.urlToImage)
        db.insert(ARTICLES_TABLE_NAME, null, values)
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