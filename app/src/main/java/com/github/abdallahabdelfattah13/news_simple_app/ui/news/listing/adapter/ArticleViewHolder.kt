package com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.ImageLoader
import com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing.NewsSingleItemActions

class ArticleViewHolder(
    private val actions: NewsSingleItemActions,
    private val imageLoader: ImageLoader,
    itemView: View
) :
    RecyclerView.ViewHolder(itemView) {

    private var article: Article? = null

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

    init {
        itemView.setOnClickListener {
            article?.let { actions.onNewsItemClick(it) }
        }
    }

    fun bind(article: Article) {
        this.article = article
        imageLoader.loadImageInto(article.imageUrl, newsItemImageIv)
        newsItemTitleTv.text = article.title
        newsItemSourceTv.text = article.source
        newsAuthorAndDateTv.text = getAuthorAndDate(article)
    }

    private fun getAuthorAndDate(article: Article): String {
        return "by ${article.formatAuthorName()}\n${article.formatArticleDate()}"
    }

}