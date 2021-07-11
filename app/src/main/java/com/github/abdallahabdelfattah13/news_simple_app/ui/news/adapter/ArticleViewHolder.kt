package com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.ImageLoader

class ArticleViewHolder(
    private val imageLoader: ImageLoader,
    itemView: View
) :
    RecyclerView.ViewHolder(itemView) {

    private val newsItemImageIv: ImageView by lazy {
        itemView.findViewById(R.id.news_item_iv)
    }
    private val newsItemTitleTv: TextView by lazy {
        itemView.findViewById(R.id.news_item_title_tv)
    }
    private val newsItemSourceTv: TextView by lazy {
        itemView.findViewById(R.id.news_item_source_tv)
    }
    private val newsAuthorAndDateTv: TextView by lazy {
        itemView.findViewById(R.id.news_item_author_and_date_tv)
    }

    fun bind(article: Article) {
        imageLoader.loadImageInto(article.imageUrl, newsItemImageIv)
        newsItemTitleTv.text = article.title
        newsItemSourceTv.text = article.source
        newsAuthorAndDateTv.text = getAuthorAndDate(article)
    }

    private fun getAuthorAndDate(article: Article): String {
        return "by ${article.formatAuthorName()}\n${article.formatArticleDate()}"
    }

}