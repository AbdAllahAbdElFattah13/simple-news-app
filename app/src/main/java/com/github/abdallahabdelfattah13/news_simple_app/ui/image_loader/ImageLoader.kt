package com.github.abdallahabdelfattah13.news_simple_app.ui.image_loader

import android.widget.ImageView

interface ImageLoader {
    fun loadImageInto(imageUrl: String?, imageView: ImageView)
}