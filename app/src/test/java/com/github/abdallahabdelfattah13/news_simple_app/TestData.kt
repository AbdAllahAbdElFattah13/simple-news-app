package com.github.abdallahabdelfattah13.news_simple_app

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.kwabenaberko.newsapilib.models.Source
import java.text.SimpleDateFormat
import java.util.*

object TestData {
    private const val source = "a-source"
    private const val author = "author-name"
    private const val title = "a-title"
    private const val description = "some-long-description"
    private const val url = "www.example.com/article1"
    private const val urlToImage = "www.example.com/article1/image1"
    private const val publishedAt = "2021-06-12T06:14:00Z"
    private val publishedAtDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        .parse(publishedAt)!!

    fun createNewsArticle(
        source: String = this.source,
        author: String = this.author,
        title: String = this.title,
        description: String = this.description,
        url: String = this.url,
        urlToImage: String = this.urlToImage,
        publishedAtDate: Date = this.publishedAtDate
    ): Article {
        return Article(
            source = source,
            author = author,
            title = title,
            description = description,
            articleUrl = url,
            imageUrl = urlToImage,
            date = publishedAtDate,
            content = description
        )
    }

    fun createClientArticle(
        source: String = this.source,
        author: String = this.author,
        title: String = this.title,
        description: String = this.description,
        url: String = this.url,
        urlToImage: String = this.urlToImage,
        publishedAt: String = this.publishedAt
    ): com.kwabenaberko.newsapilib.models.Article {
        return com.kwabenaberko.newsapilib.models.Article().apply {
            this.author = author
            this.source = Source().apply { this.name = source }
            this.title = title
            this.description = description
            this.publishedAt = publishedAt
            this.url = url
            this.urlToImage = urlToImage
        }
    }
}