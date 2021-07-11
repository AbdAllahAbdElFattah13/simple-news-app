package com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import java.text.SimpleDateFormat
import java.util.*


fun ArticleResponse.toNewsArticleResponse(): NewsArticlesResponse {
    val articles = this.articles
        .filterNotNull()
        .map { it.toArticle() }

    return NewsArticlesResponse(
        status = this.status,
        totalResultsCount = this.totalResults,
        articles = articles
    )
}

private fun com.kwabenaberko.newsapilib.models.Article.toArticle(): Article {
    return Article(
        source = this.source.name ?: "",
        author = this.author ?: "",
        title = this.title ?: "",
        description = this.description ?: "",
        articleUrl = this.url ?: "",
        imageUrl = this.urlToImage ?: "",
        date = this.publishedAt.toDate(),
        content = this.description ?: ""
    )
}

private fun String.toDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    return sdf.parse(this)

}
