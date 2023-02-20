package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.News
import java.text.SimpleDateFormat
import java.util.*

data class NaverNewsResponse(
    @SerializedName("items") val items: List<NaverNews>
)

data class NaverNews(
    @SerializedName("title") val title: String,
    @SerializedName("link") val url: String,
    @SerializedName("description") val contents: String,
    @SerializedName("pubDate") val date: String
) {
    fun toNews(): News {
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate =
            format.parse(date)
                ?.let { targetFormat.format(it) }
                ?: date


        return News(
            title = title,
            date = formattedDate,
            imageUrl = "",
            contents = contents,
            url = url
        )
    }
}