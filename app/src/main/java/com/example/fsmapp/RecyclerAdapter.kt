package com.example.fsmapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.Executors

class RecyclerAdapter(private val articleList: ArrayList<NewsResult.Article>):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(articleList[position])
    }

    override fun getItemCount() = articleList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var articleTitle: TextView = itemView.findViewById(R.id.article_title)
        private var articleSource: TextView = itemView.findViewById(R.id.article_author)
        private var articleDate: TextView = itemView.findViewById(R.id.article_source)
        private var articleImage: ImageView = itemView.findViewById(R.id.article_image)

        fun bindItems(article: NewsResult.Article) {
            articleTitle.text = article.title
            articleSource.text = article.source.name
            //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:")
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            var unformattedDate = article.publishedAt
            unformattedDate = unformattedDate.substring(0, unformattedDate.length-1)
            val date = LocalDateTime.parse(unformattedDate)
            articleDate.text = date.format(formatter)

            // Declaring executor to parse the URL
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap?
            executor.execute {
                try {
                    val `in` = java.net.URL(article[3]).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        articleImage.setImageBitmap(image)
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}