package com.github.abdallahabdelfattah13.news_simple_app.di

import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository.DefaultNewsRepository
import com.github.abdallahabdelfattah13.news_simple_app.presentation.news.NewsViewModelFactory
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.FakeNewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client.ClientBasedNewsService
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.ImageLoader
import com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader.PicassoImageLoader
import com.kwabenaberko.newsapilib.NewsApiClient
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DependenciesProvider {

    private fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    private fun provideAndroidMainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    private fun provideFakeNewsService(): NewsService {
        return FakeNewsService()
    }

    private fun provideLiveNewsService(
        apiKey: String
    ): NewsService {
        return ClientBasedNewsService(NewsApiClient(apiKey))
    }

    private fun provideNewsRepository(
        service: NewsService,
        ioScheduler: Scheduler
    ): NewsRepository {
        return DefaultNewsRepository(
            newsService = service,
            ioScheduler = ioScheduler
        )
    }

    fun provideNewsViewModelFactory(
        apiKey: String
    ): NewsViewModelFactory {
        val ioScheduler = provideIoScheduler()
        val androidMainThread = provideAndroidMainThread()

        return provideNewsViewModelFactory(
            apiKey,
            ioScheduler,
            androidMainThread
        )
    }

    fun provideNewsViewModelFactory(
        apiKey: String,
        ioScheduler: Scheduler,
        androidMainThread: Scheduler
    ): NewsViewModelFactory {
        return NewsViewModelFactory(
            provideNewsRepository(provideLiveNewsService(apiKey), ioScheduler),
            ioScheduler,
            androidMainThread
        )
    }

    fun providerImageLoader(): ImageLoader = PicassoImageLoader()
}