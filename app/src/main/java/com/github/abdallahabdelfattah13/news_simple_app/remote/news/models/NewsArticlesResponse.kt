package com.github.abdallahabdelfattah13.news_simple_app.remote.news.models

data class NewsArticlesResponse(
    val status: String,
    val totalResultsCount: Int,
    val articles: List<Article>
)
