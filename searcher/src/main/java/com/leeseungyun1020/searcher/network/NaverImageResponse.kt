package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.Image

data class NaverImageResponse(
    @SerializedName("items") val items: List<NaverImage>
)

data class NaverImage(
    @SerializedName("title") val title: String,
    @SerializedName("link") val url: String,
    @SerializedName("thumbnail") val imageUrl: String,
) {
    fun toImage() = Image(
        title, imageUrl, url
    )
}