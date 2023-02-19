package com.leeseungyun1020.searcher.network

import android.util.Log
import com.google.gson.Gson
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkManager {
    private var type = Type.NAVER
    private var id = ""
    private var pw = ""

    fun init(type: Type, id: String, pw: String) {
        this.type = type
        this.id = id
        this.pw = pw
    }

    fun init(type: Type, pw: String) {
        this.type = type
        this.pw = pw
    }

    suspend fun getResponseFromUrl(url: String): String = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "GET"
            when (type) {
                Type.NAVER -> {
                    connection.setRequestProperty("X-Naver-Client-Id", id)
                    connection.setRequestProperty("X-Naver-Client-Secret", pw)
                }
                Type.KAKAO -> {
                    TODO("KAKAO SETTING")
                }
            }
            connect()
        }

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("HTTP request failed, error code: $responseCode")
        }

        val inputReader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = inputReader.use(BufferedReader::readText)
        inputReader.close()
        connection.disconnect()

        response
    }

    suspend fun loadNews(
        keyword: String,
        page: Int,
        display: Int,
        onSuccess: (newsList: List<News>) -> Unit,
        onFailure: ((message: String) -> Unit)? = null
    ) {
        try {
            when (type) {
                Type.NAVER -> {
                    val response = getResponseFromUrl(
                        Urls.NAVER_NEWS
                            .addQuery("query", keyword)
                            .addQuery("display", "$display")
                            .addQuery("start", "${1 + display * page}")
                            .toString()
                    )
                    val naverNewsResponse = Gson().fromJson(response, NaverNewsResponse::class.java)
                    onSuccess(naverNewsResponse.items.map { it.toNews() })
                }
                Type.KAKAO -> {
                    TODO("Kakao NEWS")
                }
            }
        } catch (e: Exception) {
            if (onFailure != null) {
                onFailure("NetworkManager: ${e.message}")
            } else {
                Log.e(TAG, "NetworkManager: ${e.message}")
            }
        }
    }

    suspend fun loadImages(
        keyword: String,
        page: Int,
        display: Int,
        onSuccess: (imageList: List<Image>) -> Unit,
        onFailure: ((message: String) -> Unit)? = null
    ) {
        try {
            when (type) {
                Type.NAVER -> {
                    val response = getResponseFromUrl(
                        Urls.NAVER_IMAGE
                            .addQuery("query", keyword)
                            .addQuery("display", "$display")
                            .addQuery("start", "${1 + display * page}")
                            .toString()
                    )
                    val naverImageResponse = Gson().fromJson(response, NaverImageResponse::class.java)
                    onSuccess(naverImageResponse.items.map { it.toImage() })
                }
                Type.KAKAO -> {
                    TODO("Kakao IMAGE")
                }
            }
        } catch (e: Exception) {
            if (onFailure != null) {
                onFailure("NetworkManager: ${e.message}")
            } else {
                Log.e(TAG, "NetworkManager: ${e.message}")
            }
        }
    }
}