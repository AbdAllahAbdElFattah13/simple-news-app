package com.github.abdallahabdelfattah13.news_simple_app.remote.news

import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import io.reactivex.Single

interface NewsService {

    fun requestNews(
        q: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<NewsArticlesResponse>

}