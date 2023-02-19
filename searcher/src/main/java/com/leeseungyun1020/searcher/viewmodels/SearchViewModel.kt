package com.leeseungyun1020.searcher.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.ImageResult
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.data.NewsResult
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Mode
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

    private val _imageResult = MutableStateFlow(ImageResult(emptyList(), Mode.REPLACE, 0))
    val imageResult = _imageResult.asStateFlow()
    private var imagePage = 0
    private var canImageLoading = true
    private val imageDisplay = 30

    private val _newsResult = MutableStateFlow(NewsResult(emptyList(), Mode.REPLACE, 0))
    val newsResult = _newsResult.asStateFlow()
    private var newsPage = 0
    private var canNewsLoading = true
    private val newsDisplay = 30

    fun search(keyword: String) {
        if (keyword.isNotBlank() && keyword != _keyword.value) {
            _keyword.value = keyword
            newsPage = 0
            imagePage = 0
            canImageLoading = true
            canNewsLoading = true
            loadResult()
        }
    }

    fun onCategoryButtonClicked(category: ResultCategory) {
        if (location.value != category)
            _location.value = category
    }

    private fun loadResult() {
        viewModelScope.launch {
            canImageLoading = false
            canNewsLoading = false
            NetworkManager.loadNews(keyword.value, 0, newsDisplay, {
                _newsResult.value = NewsResult(it, Mode.REPLACE, 0)
                if (it.size == newsDisplay) {
                    canNewsLoading = true
                }
            })
            NetworkManager.loadImages(keyword.value, 0, imageDisplay, {
                _imageResult.value = ImageResult(it, Mode.REPLACE, 0)
                if (it.size == imageDisplay) {
                    canImageLoading = true
                }
            })
        }
    }

    fun loadMoreImage() {
        viewModelScope.launch {
            if (canImageLoading) {
                Log.d(TAG, "loadMoreImage: $imagePage")
                canImageLoading = false
                imagePage += 1
                NetworkManager.loadImages(keyword.value, imagePage, imageDisplay, {
                    if (it.isNotEmpty()) {
                        val prev = imageResult.value.images
                        _imageResult.value = ImageResult(prev + it, Mode.ADD, prev.size)
                    }
                    if (it.size == imageDisplay) {
                        canImageLoading = true
                    }
                })
            }
        }
    }

    fun loadMoreNews() {
        viewModelScope.launch {
            if (canNewsLoading) {
                canNewsLoading = false
                newsPage += 1
                NetworkManager.loadNews(keyword.value, newsPage, newsDisplay, {
                    if (it.isNotEmpty()) {
                        val prev = newsResult.value.news
                        _newsResult.value = NewsResult(prev + it, Mode.ADD, prev.size)
                    }
                    if (it.size == newsDisplay) {
                        canNewsLoading = true
                    }
                })
            }
        }
    }

}