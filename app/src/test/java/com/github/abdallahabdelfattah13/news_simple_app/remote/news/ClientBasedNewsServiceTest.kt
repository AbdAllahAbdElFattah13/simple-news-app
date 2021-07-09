package com.github.abdallahabdelfattah13.news_simple_app.remote.news

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client.ClientBasedNewsService
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Source
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class ClientBasedNewsServiceTest {

    private val source = "a-source"
    private val author = "author-name"
    private val title = "a-title"
    private val description = "some-long-description"
    private val url = "www.example.com/article1"
    private val urlToImage = "www.example.com/article1/image1"
    private val publishedAt = "2021-06-12T06:14:00Z"
    private val publishedAtDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        .parse(publishedAt)!!

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
                this.articles = listOf(createClientArticle())
            }
        val expectedResponse = NewsArticlesResponse(
            status = status,
            totalResultsCount = totalCount,
            articles = listOf(createNewsArical())
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

    @Test
    fun f() {
        val total = 14592
        val page_size = 30

        val final_page = ceil(total.toDouble() / page_size).toInt()

        Assert.assertEquals(487, final_page)
    }

    private fun createClientArticle(
        source: String = this.source,
        author: String = this.author,
        title: String = this.title,
        description: String = this.description,
        url: String = this.url,
        urlToImage: String = this.urlToImage,
        publishedAt: String = this.publishedAt
    ): com.kwabenaberko.newsapilib.models.Article {
        return com.kwabenaberko.newsapilib.models.Article().apply {
            this.author = author
            this.source = Source().apply { this.name = source }
            this.title = title
            this.description = description
            this.publishedAt = publishedAt
            this.url = url
            this.urlToImage = urlToImage
        }
    }

    private fun createNewsArical(
        source: String = this.source,
        author: String = this.author,
        title: String = this.title,
        description: String = this.description,
        url: String = this.url,
        urlToImage: String = this.urlToImage,
        publishedAtDate: Date = this.publishedAtDate
    ): Article {
        return Article(
            source = source,
            author = author,
            title = title,
            description = description,
            articleUrl = url,
            imageUrl = urlToImage,
            date = publishedAtDate,
            content = description
        )
    }
}