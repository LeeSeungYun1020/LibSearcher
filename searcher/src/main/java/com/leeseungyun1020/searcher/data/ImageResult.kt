package com.leeseungyun1020.searcher.data

import com.leeseungyun1020.searcher.utilities.Mode

data class ImageResult(
    val images: List<Image>,
    val mode: Mode,
    val start: Int
)