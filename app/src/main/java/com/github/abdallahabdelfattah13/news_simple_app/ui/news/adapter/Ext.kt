package com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import java.text.SimpleDateFormat
import java.util.*

fun Article.formatAuthorName(): String {
    var articleAuthor = this.author
    if (articleAuthor.contains('/'))
        articleAuthor = articleAuthor
            .split('/')
            .last()
            .replace('-', ' ')
            .split(' ')
            .joinToString(separator = " ") { it.capitalize() }

    return articleAuthor
}

fun Article.formatArticleDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.US)
    return dateFormat.format(this.date)
}