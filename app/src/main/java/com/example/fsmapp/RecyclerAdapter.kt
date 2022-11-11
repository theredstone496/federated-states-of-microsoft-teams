package com.example.fsmapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 * @param articleList ArrayList of Arrays. Pass arguments into Array in order: title, author, source.
 * */
class RecyclerAdapter(private val articleList: ArrayList<ArrayList<String>>):
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
        private var articleAuthor: TextView = itemView.findViewById(R.id.article_author)
        private var articleSource: TextView = itemView.findViewById(R.id.article_source)

        fun bindItems(article: ArrayList<String>) {
            articleTitle.text = article[0]
            articleAuthor.text = article[1]
            articleSource.text = article[2]
        }
    }
}