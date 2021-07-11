package com.github.abdallahabdelfattah13.news_simple_app.ui.news.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.abdallahabdelfattah13.news_simple_app.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class NewsItemFragment : Fragment() {

    private val args: NewsItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailsWv = view.findViewById<WebView>(R.id.news_item_details_wv)
        val detailsCpi =
            view.findViewById<CircularProgressIndicator>(R.id.news_item_details_loading_cpi)

        detailsWv.settings.javaScriptEnabled = true
        detailsWv.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                detailsCpi.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                detailsCpi.hide()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        detailsWv.loadUrl(args.newsItemUrl)
    }
}