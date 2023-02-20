package com.leeseungyun1020.searcher.network

import com.google.gson.annotations.SerializedName
import com.leeseungyun1020.searcher.data.News
import java.text.SimpleDateFormat
import java.util.*

data class KakaoNewsResponse(
    @SerializedName("documents") val items: List<KakaoNews>
)

data class KakaoNews(
    @SerializedName("title") val title: String,
    @SerializedName("contents") val contents: String,
    @SerializedName("url") val url: String,
    @SerializedName("datetime") val date: String
) {
    fun toNews(): News {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
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