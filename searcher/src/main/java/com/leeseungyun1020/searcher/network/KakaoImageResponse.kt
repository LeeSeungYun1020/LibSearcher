package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.Image

data class KakaoImageResponse(
    @SerializedName("documents") val items: List<KakaoImage>
)

data class KakaoImage(
    @SerializedName("thumbnail_url") val imageUrl: String,
    @SerializedName("doc_url") val url: String,
    @SerializedName("display_sitename") val title: String,
) {
    fun toImage() = Image(
        title, imageUrl, url
    )
}
