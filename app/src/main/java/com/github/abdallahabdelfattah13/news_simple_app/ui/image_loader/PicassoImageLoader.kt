package com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader

import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicassoImageLoader : ImageLoader {
    override fun loadImageInto(imageUrl: String?, imageView: ImageView) {
        val picassoLoadingUrl = if (imageUrl.isNullOrBlank()) null else imageUrl

        Picasso
            .get()
            .load(picassoLoadingUrl)
            .fit()
            .centerCrop()
            .into(imageView)
    }
}