package com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article

interface NewsSingleItemActions {
    fun onNewsItemClick(article: Article)
}