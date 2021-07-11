package com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.ImageLoader
import com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing.NewsSingleItemActions

class NewsAdapter(
    private val actions: NewsSingleItemActions,
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
            actions,
            imageLoader,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false),
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}