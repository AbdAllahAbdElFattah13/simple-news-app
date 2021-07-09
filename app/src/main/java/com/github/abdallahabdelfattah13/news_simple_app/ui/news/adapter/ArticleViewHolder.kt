package com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article

class ArticleViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(article: Article?) {
        itemView.findViewById<TextView>(R.id.title_tv)
            .text = article?.title
    }

}