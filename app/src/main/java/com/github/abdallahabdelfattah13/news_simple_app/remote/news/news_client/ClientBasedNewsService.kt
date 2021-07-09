package com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client

import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import io.reactivex.Single
import io.reactivex.SingleEmitter

class ClientBasedNewsService(
    private val client: NewsApiClient
) : NewsService {

    override fun requestNews(
        q: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<NewsArticlesResponse> {
        return Single.create { source ->
            requestNews(
                q,
                pageNumber,
                pageSize,
                SingleAdapter(source)
            )
        }
    }

    private fun requestNews(
        q: String,
        pageNumber: Int,
        pageSize: Int,
        listener: NewsApiClient.ArticlesResponseCallback
    ) {
        val request = EverythingRequest
            .Builder()
                .q(q)
                .page(pageNumber)
                .pageSize(pageSize)
            .build()

        client.getEverything(request, listener)
    }

    private class SingleAdapter(
        private val singleSource: SingleEmitter<NewsArticlesResponse>
    ) : NewsApiClient.ArticlesResponseCallback {
        override fun onSuccess(response: ArticleResponse) {
            val mappedResponse = response.toNewsArticleResponse()
            singleSource.onSuccess(mappedResponse)
        }

        override fun onFailure(throwable: Throwable) {
            singleSource.onError(throwable)
        }

    }

}