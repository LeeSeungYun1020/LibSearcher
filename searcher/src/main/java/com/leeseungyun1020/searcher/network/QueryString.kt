package com.leeseungyun1020.searcher.network

class QueryString(val string: String) {
    fun addQuery(key: String, value: String) = QueryString("$this&$key=$value")

    override fun toString() = string
}

fun String.addQuery(key: String, value: String) = QueryString("$this?$key=$value")