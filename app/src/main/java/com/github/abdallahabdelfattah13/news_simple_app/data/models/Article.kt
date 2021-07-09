package com.github.abdallahabdelfattah13.news_simple_app.data.models

import java.util.*

data class Article(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val articleUrl: String,
    val imageUrl: String,
    val date: Date,
    val content: String
)
