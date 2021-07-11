package com.github.abdallahabdelfattah13.news_simple_app.data.news

import androidx.paging.PagingData
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import io.reactivex.Flowable

interface NewsRepository {

    fun searchNews(q: String): Flowable<PagingData<Article>>

}