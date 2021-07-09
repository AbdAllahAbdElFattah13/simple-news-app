package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val backgroundScheduler: Scheduler
) : ViewModel() {

    val searchQueryStream: Subject<String> by lazy {
        PublishSubject.create()
    }

    val data: LiveData<PagingData<Article>> by lazy {
        val source: Flowable<PagingData<Article>> = searchQueryStream
            .toFlowable(BackpressureStrategy.DROP)
            .flatMap { newsRepository.searchNews(it) }

        LiveDataReactiveStreams.fromPublisher(source)
    }

}