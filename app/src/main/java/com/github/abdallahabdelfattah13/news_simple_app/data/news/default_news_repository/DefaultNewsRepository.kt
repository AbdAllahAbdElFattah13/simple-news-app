package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsConstants
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import io.reactivex.Flowable
import io.reactivex.Scheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DefaultNewsRepository(
    private val newsService: NewsService,
    private val ioScheduler: Scheduler
) : NewsRepository {

    @ExperimentalCoroutinesApi
    override fun searchNews(q: String)
            : Flowable<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsConstants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(newsService, ioScheduler, q) }
        ).flowable
    }

}