package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val backgroundScheduler: Scheduler
) : ViewModel() {

    private val _state = MutableLiveData<PagingData<Article>>()
    val state: LiveData<PagingData<Article>>
        get() = _state

    private val compositeDisposables = CompositeDisposable()

    @ExperimentalCoroutinesApi
    fun searchForQ(q: String) {
        val x = newsRepository
            .searchNews(q)
            .cachedIn(viewModelScope)
            .subscribeOn(backgroundScheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _state.value = it
            }
    }

    @ExperimentalCoroutinesApi
    fun searchForQ2(q: String): Flowable<PagingData<Article>> {
        return newsRepository.searchNews(q).cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.dispose()
    }

}