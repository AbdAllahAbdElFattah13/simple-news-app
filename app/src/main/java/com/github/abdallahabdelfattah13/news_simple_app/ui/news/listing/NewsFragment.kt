package com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.github.abdallahabdelfattah13.news_simple_app.BuildConfig
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.github.abdallahabdelfattah13.news_simple_app.data.models.Article
import com.github.abdallahabdelfattah13.news_simple_app.di.DependenciesProvider
import com.github.abdallahabdelfattah13.news_simple_app.presentation.news.NewsViewModel
import com.github.abdallahabdelfattah13.news_simple_app.ui.news.listing.adapter.NewsAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit

class NewsFragment : Fragment() {

    private val di: DependenciesProvider by lazy { DependenciesProvider }

    private val actions: NewsSingleItemActions by lazy {
        object : NewsSingleItemActions {
            override fun onNewsItemClick(article: Article) {
                val navAction =
                    NewsFragmentDirections.actionNewsFragmentToNewsItemFragment(article.articleUrl)
                findNavController().navigate(navAction)
            }

        }
    }
    private val adapter: NewsAdapter by lazy { NewsAdapter(actions, di.providerImageLoader()) }

    private val vm: NewsViewModel by viewModels {
        di.provideNewsViewModelFactory(BuildConfig.newsApiKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEt = requireView().findViewById<EditText>(R.id.search_ed)
        val newsRv = requireView().findViewById<RecyclerView>(R.id.news_rv)

        newsRv.adapter = adapter

        RxTextView
            .afterTextChangeEvents(searchEt)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe { vm.querySubject.onNext(it?.editable()?.toString() ?: "") }

        vm.state.observe(viewLifecycleOwner, ::renderPage)
    }

    private fun renderPage(page: PagingData<Article>) {
        adapter.submitData(lifecycle, page)
    }
}