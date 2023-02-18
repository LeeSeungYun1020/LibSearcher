package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.Image

data class NaverImageResponse(
    val items: List<NaverImage>
)

data class NaverImage(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("thumbnail") val thumbnail: String,
) {
    fun toImage(): Image = Image(
        title = title,
        imageUrl = thumbnail,
        url = link,
    )
}