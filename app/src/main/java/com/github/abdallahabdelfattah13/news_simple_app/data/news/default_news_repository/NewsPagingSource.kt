package com.github.abdallahabdelfattah13.news_simple_app.data.news.default_news_repository


import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.data.news.NewsConstants
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import io.reactivex.Scheduler
import io.reactivex.Single

class NewsPagingSource(
    private val newsService: NewsService,
    private val ioScheduler: Scheduler,
    private val q: String
) : RxPagingSource<Int, Article>() {

    override fun loadSingle(params: LoadParams<Int>)
            : Single<LoadResult<Int, Article>> {

        val currentPage = params.key ?: NewsConstants.FIRST_PAGE_INDEX

        return newsService.
        requestNews(q, currentPage, params.loadSize)
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

}