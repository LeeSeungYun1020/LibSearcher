package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.News

data class NaverNewsResponse(
    val items: List<NaverNews>
)

data class NaverNews(
    @SerializedName("title") val title: String,
    @SerializedName("link") val url: String,
    @SerializedName("description") val contents: String,
    @SerializedName("pubDate") val date: String
) {
    fun toNews(): News = News(
        title = title,
        date = date,
        imageUrl = "",
        contents = contents,
        url = url
    )
}