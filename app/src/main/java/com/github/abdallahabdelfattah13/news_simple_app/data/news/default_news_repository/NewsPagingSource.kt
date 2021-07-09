package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository


import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsConstants
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.internal.schedulers.IoScheduler
import kotlin.math.ceil

//AbdAllah: missing test cases for now
class NewsPagingSource(
    private val newsService: NewsService,
    private val ioScheduler: Scheduler,
    private val q: String
) : RxPagingSource<Int, Article>() {

    override fun loadSingle(params: LoadParams<Int>)
            : Single<LoadResult<Int, Article>> {

        val currentPage = params.key ?: NewsConstants.FIRST_PAGE_INDEX

        return newsService.requestNews(q, currentPage, NewsConstants.PAGE_SIZE)
            .map {
                toLoadPage(
                    it, currentPage, params.loadSize,
                    NewsConstants.FIRST_PAGE_INDEX, NewsConstants.PAGE_SIZE
                )
            }
            .onErrorReturn { toLoadError(it) }
            .subscribeOn(ioScheduler)
    }

    override fun getRefreshKey(state: PagingState<Int, Article>)
            : Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun toLoadPage(
        response: NewsArticlesResponse,
        currentPage: Int,
        currentLoadSize: Int,
        firstPageIndex: Int,
        pageSize: Int
    ): LoadResult<Int, Article> {
        val maxPageNumber = getMaxPageNumber(response.totalResultsCount, pageSize)

        val previousPage = if (currentPage == firstPageIndex) null else currentPage - 1
        val nextPage =
            if (currentPage >= maxPageNumber) null else currentPage + (currentLoadSize / pageSize)

        return LoadResult.Page(
            prevKey = previousPage,
            nextKey = nextPage,
            data = response.articles
        )
    }

    private fun toLoadError(
        throwable: Throwable
    ): LoadResult<Int, Article> {
        return LoadResult.Error(throwable)
    }

    private fun getMaxPageNumber(
        totalResultsCount: Int,
        pageSize: Int
    ): Int {
        return ceil(totalResultsCount.toDouble() / pageSize).toInt()
    }
}