package com.leeseungyun1020.searcher.network

import com.google.gson.Gson
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

    suspend inline fun <reified T> getDataFromUrl(url: String): T = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("HTTP request failed, error code: $responseCode")
        }

        val inputReader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = inputReader.use(BufferedReader::readText)
        inputReader.close()
        connection.disconnect()

        Gson().fromJson(response, T::class.java)
    }
}