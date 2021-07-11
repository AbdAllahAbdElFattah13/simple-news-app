package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.abdallahabdelfattah13.news_simple_app.TestData
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NewsPagingSourceTest {

    @Test
    fun `loadSingle - first page`() {
        val newsService: NewsService = mock()
        val scheduler = Schedulers.trampoline()
        val pageNumber = 1
        val pageSize = 20
        val loadSize = 3 * pageSize
        val totalResultsCount = 1000
        val q = "android"
        val params: PagingSource.LoadParams<Int> =
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        val articles = listOf(TestData.createNewsArticle())

        val response = NewsArticlesResponse(
            status = "ok",
            totalResultsCount = totalResultsCount,
            articles = articles
        )
        whenever(newsService.requestNews(q, pageNumber, loadSize))
            .thenReturn(Single.just(response))

        val expectedResult = PagingSource.LoadResult.Page(
            prevKey = null,
            nextKey = 4,
            data = articles
        )

        val newsPagingSource = NewsPagingSource(newsService, scheduler, q)

        newsPagingSource
            .loadSingle(params)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResult)
    }

    @Test
    fun `loadSingle - success`() {
        val newsService: NewsService = mock()
        val scheduler = Schedulers.trampoline()
        val pageNumber = 5
        val pageSize = 20
        val loadSize = pageSize
        val totalResultsCount = 1000
        val q = "android"
        val params: PagingSource.LoadParams<Int> =
            PagingSource.LoadParams.Append(
                key = pageNumber,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        val articles = listOf(TestData.createNewsArticle())

        val response = NewsArticlesResponse(
            status = "ok",
            totalResultsCount = totalResultsCount,
            articles = articles
        )
        whenever(newsService.requestNews(q, pageNumber, loadSize))
            .thenReturn(Single.just(response))

        val expectedResult = PagingSource.LoadResult.Page(
            prevKey = 4,
            nextKey = 6,
            data = articles
        )

        val newsPagingSource = NewsPagingSource(newsService, scheduler, q)

        newsPagingSource
            .loadSingle(params)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResult)
    }

    @Test
    fun `loadSingle - fail`() {
        val newsService: NewsService = mock()
        val scheduler = Schedulers.trampoline()
        val pageNumber = 5
        val pageSize = 20
        val loadSize = pageSize
        val q = "android"
        val params: PagingSource.LoadParams<Int> =
            PagingSource.LoadParams.Append(
                key = pageNumber,
                loadSize = loadSize,
                placeholdersEnabled = false
            )

        val error = IllegalArgumentException("Passed above the max results number")
        whenever(newsService.requestNews(q, pageNumber, loadSize))
            .thenReturn(Single.error(error))

        val expectedResult = PagingSource.LoadResult.Error<Int, Article>(error)

        val newsPagingSource = NewsPagingSource(newsService, scheduler, q)

        newsPagingSource
            .loadSingle(params)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResult)
    }

    @Test
    fun `getRefreshKey - can't locate previous anchor position - returns null`() {
        val state: PagingState<Int, Article> = mock()
        whenever(state.anchorPosition).thenReturn(null)
        val expectedRefreshKey = null
        val actualRefreshKey = NewsPagingSource(mock(), mock(), "").getRefreshKey(state)
        Assert.assertEquals(expectedRefreshKey, actualRefreshKey)

    }

    @Test
    fun `getRefreshKey - finds prevKey - returns prevKey + 1`() {
        val anchorPosition = 41
        val prevKey = 3
        val state: PagingState<Int, Article> = mock()
        val page: PagingSource.LoadResult.Page<Int, Article> = PagingSource.LoadResult.Page(
            prevKey = prevKey,
            nextKey = null,
            data = emptyList()
        )
        whenever(state.anchorPosition).thenReturn(anchorPosition)
        whenever(state.closestPageToPosition(anchorPosition)).thenReturn(page)
        val expectedRefreshKey = prevKey + 1
        val actualRefreshKey = NewsPagingSource(mock(), mock(), "").getRefreshKey(state)
        Assert.assertEquals(expectedRefreshKey, actualRefreshKey)
    }

    @Test
    fun `getRefreshKey - doesn't find prevKey, but next key - returns nextKey - 1`() {
        val anchorPosition = 41
        val nextKey = 5
        val state: PagingState<Int, Article> = mock()
        val page: PagingSource.LoadResult.Page<Int, Article> = PagingSource.LoadResult.Page(
            prevKey = null,
            nextKey = nextKey,
            data = emptyList()
        )
        whenever(state.anchorPosition).thenReturn(anchorPosition)
        whenever(state.closestPageToPosition(anchorPosition)).thenReturn(page)
        val expectedRefreshKey = nextKey - 1
        val actualRefreshKey = NewsPagingSource(mock(), mock(), "").getRefreshKey(state)
        Assert.assertEquals(expectedRefreshKey, actualRefreshKey)
    }
}