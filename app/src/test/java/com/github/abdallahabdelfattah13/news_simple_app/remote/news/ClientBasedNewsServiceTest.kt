package com.github.abdallahabdelfattah13.news_simple_app.remote.news

import com.github.abdallahabdelfattah13.news_simple_app.TestData
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client.ClientBasedNewsService
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

class ClientBasedNewsServiceTest {


    @Test
    fun `requestNews - success - returns single with data`() {
        val q = "android"
        val pageNumber = 1
        val pageSize = 30
        val status = "ok"
        val totalCount = 1954

        val returnedResponse: ArticleResponse = ArticleResponse()
            .apply {
                this.status = status
                this.totalResults = totalCount
                this.articles = listOf(TestData.createClientArticle())
            }
        val expectedResponse = NewsArticlesResponse(
            status = status,
            totalResultsCount = totalCount,
            articles = listOf(TestData.createNewsArticle())
        )

        val client: NewsApiClient = mock()
        val everythingRequest: EverythingRequest = argThat {
            q == this.q &&
                    pageNumber.toString() == this.page &&
                    pageSize.toString() == this.pageSize
        }
        whenever(client.getEverything(everythingRequest, any()))
            .thenAnswer { invocation ->
                (invocation.getArgument(1) as NewsApiClient.ArticlesResponseCallback)
                    .onSuccess(returnedResponse)
            }

        val remoteNewsClient = ClientBasedNewsService(client)

        remoteNewsClient
            .requestNews(q, pageNumber, pageSize)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResponse)
    }

    @Test
    fun `requestNews - error - returns single with error`() {
        val q = "android"
        val pageNumber = 1
        val pageSize = 30

        val error = IOException("failed to connect")
        val client: NewsApiClient = mock()
        val everythingRequest: EverythingRequest = argThat {
            q == this.q &&
                    pageNumber.toString() == this.page &&
                    pageSize.toString() == this.pageSize
        }
        whenever(client.getEverything(everythingRequest, any()))
            .thenAnswer { invocation ->
                (invocation.getArgument(1) as NewsApiClient.ArticlesResponseCallback)
                    .onFailure(error)
            }

        val remoteNewsClient = ClientBasedNewsService(client)

        remoteNewsClient
            .requestNews(q, pageNumber, pageSize)
            .test()
            .assertNotComplete()
            .assertFailure(error::class.java)
            .assertError(error)
    }
}