package com.github.abdallahabdelfattah13.news_simple_app.remote.news

import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.models.NewsArticlesResponse
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

class FakeNewsService : NewsService {
    override fun requestNews(
        q: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<NewsArticlesResponse> {
        val sampleArticles = listOf(
            Article(
                source = "Engadget",
                author = "https://www.engadget.com/about/editors/cherlynn-low",
                title = "Latest Android update includes starred messages and more voice controls",
                description = "Six new updates are coming to Android, including starred Messages and improved Voice Access..",
                articleUrl = "https://www.engadget.com/android-update-june-2021-starred-messages-earthquake-detection-alert-voice-access-password-160100755.html",
                imageUrl = "https://s.yimg.com/os/creatr-uploaded-images/2021-06/20df9350-cd6b-11eb-bbde-30a616490004",
                date = Date(),
                content = "Six new updates are coming to Android, including starred Messages and improved Voice Access.."
            ), Article(
                source = "Engadget",
                author = "https://www.engadget.com/about/editors/saqib-shah",
                title = "Spotify launches its Clubhouse rival, Greenroom",
                description = "Spotify is launching its live audio app on iOS and Android in 135 markets..",
                articleUrl = "https://www.engadget.com/spotify-launches-its-clubhouse-live-audio-rival-greenroom-130034550.html",
                imageUrl = "https://s.yimg.com/os/creatr-uploaded-images/2021-06/8be3d480-cea2-11eb-bf86-2f13a546c0ca",
                date = Date(),
                content = "Spotify is launching its live audio app on iOS and Android in 135 markets.."
            )
        )

        Thread.sleep(TimeUnit.SECONDS.toMillis(1))

        val firstIntRange = ((pageNumber - 1) * pageSize)
        val articles = IntRange(firstIntRange, firstIntRange + pageSize)
            .map { sampleArticles[it % 2].copy(title = "${sampleArticles[it % 2].title} $it") }

        return Single.just(
            NewsArticlesResponse(
                status = "ok",
                totalResultsCount = 1500,
                articles = articles
            )
        )
    }
}