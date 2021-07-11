package com.github.abdallahabdelfattah13.news_simple_app.presentation.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.github.abdallahabdelfattah13.news_simple_app.TestData
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*


class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val subscribeScheduler = Schedulers.trampoline()
    private val observeScheduler = Schedulers.trampoline()
    private val articlesTemplate = listOf(
        TestData.createNewsArticle(),
        TestData.createNewsArticle()
    )

    @Test
    fun `state reacts correctly to querySubject events`() {
        val q1 = "android"
        val invalidQ = ""
        val q2 = "amazon"
        val q1Articles =
            articlesTemplate.mapIndexed { index, article -> article.copy(title = "$q1 ${index + 1}") }
        val q2Articles =
            articlesTemplate.mapIndexed { index, article -> article.copy(title = "$q2 ${index + 1}") }
        val queriesQueue: Queue<String> =
            LinkedList(listOf(q1, invalidQ, q2))
        val expectedResults = listOf(q1Articles, q2Articles)

        val newsRepository: NewsRepository = mock()
        whenever(newsRepository.searchNews(q1))
            .thenReturn(Flowable.just(PagingData.from(q1Articles)))
        whenever(newsRepository.searchNews(q2))
            .thenReturn(Flowable.just(PagingData.from(q2Articles)))

        val newsViewModel = NewsViewModel(
            newsRepository,
            subscribeScheduler,
            observeScheduler
        )
        var eventsCount = 0
        newsViewModel.state.observeForever {
            eventsCount += 1
        }

        queriesQueue
            .forEach { q -> newsViewModel.querySubject.onNext(q) }

        //For now, this should do, as we still need a way to
        //capture the emitted items themselves.
        Assert.assertEquals(expectedResults.size, eventsCount)
    }
}