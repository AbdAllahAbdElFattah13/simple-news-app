package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : ViewModel() {

    val querySubject: Subject<String> by lazy {
        PublishSubject.create()
    }

    @ExperimentalCoroutinesApi
    val state: LiveData<PagingData<Article>> by lazy {
        val source = querySubject
            .filter { it.isNotBlank() }
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap { newsRepository.searchNews(it) }
            .cachedIn(viewModelScope)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)

        LiveDataReactiveStreams.fromPublisher(source)
    }

}