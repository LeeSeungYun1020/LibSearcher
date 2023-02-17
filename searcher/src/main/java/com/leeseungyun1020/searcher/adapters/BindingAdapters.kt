package com.leeseungyun1020.searcher.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.nostra13.universalimageloader.core.ImageLoader

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        ImageLoader.getInstance().displayImage(imageUrl, view)
    } else {
        view.visibility = View.GONE
    }
}