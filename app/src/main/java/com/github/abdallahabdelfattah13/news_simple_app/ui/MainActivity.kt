package com.github.abdallahabdelfattah13.news_simple_app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.github.abdallahabdelfattah13.news_simple_app.BuildConfig
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.di.DependenciesProvider
import com.github.abdallahabdelfattah13.news_simple_app.presentation.news.NewsViewModel
import com.github.abdallahabdelfattah13.news_simple_app.ui.news.adapter.NewsAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val di: DependenciesProvider by lazy { DependenciesProvider }
    private val newsRv: RecyclerView by lazy { findViewById(R.id.news_rv) }
    private val adapter: NewsAdapter by lazy { NewsAdapter(di.providerImageLoader()) }


    private val vm: NewsViewModel by viewModels {
        di.provideNewsViewModelFactory(BuildConfig.newsApiKey)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEt = findViewById<EditText>(R.id.search_ed)
        newsRv.adapter = adapter


        RxTextView
            .afterTextChangeEvents(searchEt)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe { vm.querySubject.onNext(it?.editable()?.toString() ?: "") }

        vm.state.observe(this, ::renderPage)
    }

    private fun renderPage(page: PagingData<Article>) {
        adapter.submitData(lifecycle, page)
    }

}