package com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.ImageLoader

class NewsAdapter(
    private val imageLoader: ImageLoader
) : PagingDataAdapter<Article, ArticleViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            imageLoader,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false),
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}