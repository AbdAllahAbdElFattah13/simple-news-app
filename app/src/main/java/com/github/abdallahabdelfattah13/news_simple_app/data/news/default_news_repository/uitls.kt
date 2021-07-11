package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository

import androidx.paging.PagingSource
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import kotlin.math.ceil

fun toLoadPage(
    response: NewsArticlesResponse,
    currentPage: Int,
    currentLoadSize: Int,
    firstPageIndex: Int,
    pageSize: Int
): PagingSource.LoadResult<Int, Article> {
    val maxPageNumber = getMaxPageNumber(response.totalResultsCount, pageSize)

    val previousPage = if (currentPage == firstPageIndex) null else currentPage - 1
    val nextPage =
        if (currentPage >= maxPageNumber) null else currentPage + (currentLoadSize / pageSize)

    return PagingSource.LoadResult.Page(
        prevKey = previousPage,
        nextKey = nextPage,
        data = response.articles
    )
}

fun toLoadError(
    throwable: Throwable
): PagingSource.LoadResult<Int, Article> {
    return PagingSource.LoadResult.Error(throwable)
}

fun getMaxPageNumber(
    totalResultsCount: Int,
    pageSize: Int
): Int {
    return ceil(totalResultsCount.toDouble() / pageSize).toInt()
}