package com.leeseungyun1020.searcher.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.ResultCategory
import com.leeseungyun1020.searcher.utilities.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    private val _location = MutableStateFlow(ResultCategory.NEWS)
    val location = _location.asStateFlow()

    private val _imageResult = MutableStateFlow(emptyList<Image>())
    val imageResult = _imageResult.asStateFlow()
    private var imagePage = 0

    private val _newsResult = MutableStateFlow(emptyList<News>())
    val newsResult = _newsResult.asStateFlow()
    private var newsPage = 0

    fun search(keyword: String) {
        if (keyword.isNotBlank()) {
            _keyword.value = keyword
            newsPage = 0
            imagePage = 0
            loadResult()
        }
    }

    fun onCategoryButtonClicked(category: ResultCategory) {
        if (location.value != category)
            _location.value = category
    }

    private fun loadResult() {
        viewModelScope.launch {
            NetworkManager.loadNews(keyword.value, newsPage, {
                _newsResult.value = it
            })
            NetworkManager.loadImages(keyword.value, imagePage, {
                _imageResult.value = it
            })
        }
    }
}