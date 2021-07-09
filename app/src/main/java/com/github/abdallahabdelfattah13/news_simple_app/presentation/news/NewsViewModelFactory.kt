package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.Scheduler

class NewsViewModelFactory(
    private val repository: NewsRepository,
    private val backgroundScheduler: Scheduler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository, backgroundScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}