package com.leeseungyun1020.searcher.data

import com.leeseungyun1020.searcher.utilities.Mode

data class NewsResult(
    val news: List<News>,
    val mode: Mode,
    val start: Int
)