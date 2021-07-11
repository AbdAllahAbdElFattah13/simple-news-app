package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository

import androidx.paging.PagingSource
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.*

class UitlsKtTest {

    @Test
    fun `toLoadPage - first page - prev is null`() {
        val currentPage = 1
        val pageSize = 10
        val currentLoadSize = 3 * pageSize
        val firstPageIndex = 1
        val totalResults = 100
        val articles = IntRange(1, pageSize)
            .map {
                Article(
                    source = "source-$it",
                    author = "author-$it",
                    title = "title-$it",
                    description = "description-$it",
                    articleUrl = "url-$it",
                    imageUrl = "url-$it/image",
                    content = "content-$it",
                    date = Date.from(Instant.now())

                )
            }
        val response = NewsArticlesResponse(
            status = "ok",
            totalResultsCount = totalResults,
            articles = articles
        )

        val expectedPrevKey = null
        val expectedNextKey = 4
        val expectedPage = PagingSource.LoadResult.Page(
            prevKey = expectedPrevKey,
            nextKey = expectedNextKey,
            data = articles
        )
        val actualPage = toLoadPage(
            response,
            currentPage,
            currentLoadSize,
            firstPageIndex,
            pageSize
        )

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun `toLoadPage - last page - next is null`() {
        val currentPage = 10
        val pageSize = 10
        val currentLoadSize = 10
        val firstPageIndex = 1
        val totalResults = 100
        val articles = IntRange(1, pageSize)
            .map {
                Article(
                    source = "source-$it",
                    author = "author-$it",
                    title = "title-$it",
                    description = "description-$it",
                    articleUrl = "url-$it",
                    imageUrl = "url-$it/image",
                    content = "content-$it",
                    date = Date.from(Instant.now())

                )
            }
        val response = NewsArticlesResponse(
            status = "ok",
            totalResultsCount = totalResults,
            articles = articles
        )

        val expectedPrevKey = 9
        val expectedNextKey = null
        val expectedPage = PagingSource.LoadResult.Page(
            prevKey = expectedPrevKey,
            nextKey = expectedNextKey,
            data = articles
        )
        val actualPage = toLoadPage(
            response,
            currentPage,
            currentLoadSize,
            firstPageIndex,
            pageSize
        )

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun `toLoadPage - middle page - next, prev have values`() {
        val currentPage = 5
        val pageSize = 10
        val currentLoadSize = 10
        val firstPageIndex = 1
        val totalResults = 100
        val articles = IntRange(1, pageSize)
            .map {
                Article(
                    source = "source-$it",
                    author = "author-$it",
                    title = "title-$it",
                    description = "description-$it",
                    articleUrl = "url-$it",
                    imageUrl = "url-$it/image",
                    content = "content-$it",
                    date = Date.from(Instant.now())

                )
            }
        val response = NewsArticlesResponse(
            status = "ok",
            totalResultsCount = totalResults,
            articles = articles
        )

        val expectedPrevKey = 4
        val expectedNextKey = 6
        val expectedPage = PagingSource.LoadResult.Page(
            prevKey = expectedPrevKey,
            nextKey = expectedNextKey,
            data = articles
        )
        val actualPage = toLoadPage(
            response,
            currentPage,
            currentLoadSize,
            firstPageIndex,
            pageSize
        )

        assertEquals(expectedPage, actualPage)
    }

    @Test
    fun toLoadError() {
        val throwable = IllegalStateException()
        val loadError = toLoadError(throwable)
        assert(loadError is PagingSource.LoadResult.Error)
        assertEquals(throwable, (loadError as PagingSource.LoadResult.Error).throwable)
    }

    @Test
    fun getMaxPageNumber() {
        assertEquals(20, getMaxPageNumber(30 * 20, 30))
        assertEquals(67, getMaxPageNumber(1983, 30))
    }
}