package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.News

data class KakaoNewsResponse(
    @SerializedName("documents") val items: List<KakaoNews>
)

data class KakaoNews(
    @SerializedName("title") val title: String,
    @SerializedName("contents") val contents: String,
    @SerializedName("url") val url: String,
    @SerializedName("datetime") val date: String
) {
    fun toNews() = News(
        title = title,
        date = date,
        imageUrl = "",
        contents = contents,
        url = url
    )
}