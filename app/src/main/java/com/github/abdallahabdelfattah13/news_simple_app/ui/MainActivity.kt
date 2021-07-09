package com.github.abdallahabdelfattah13.news_simple_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.NewsService
import com.github.abdallahabdelfattah13.news_simple_app.remote.news.news_client.ClientBasedNewsService
import com.kwabenaberko.newsapilib.NewsApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val service: NewsService = ClientBasedNewsService(
            NewsApiClient(BuildConfig.newsApiKey)
        )

        val x = service.requestNews(
            "android",
            pageNumber = 1,
            pageSize = 40
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                findViewById<TextView>(R.id.output_tv)
                    .text = response.toString()
            }
    }
}