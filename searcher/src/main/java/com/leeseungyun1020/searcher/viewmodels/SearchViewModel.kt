package com.leeseungyun1020.searcher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.News
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    private val _imageResult = MutableStateFlow(emptyList<Image>())
    val imageResult = _imageResult.asStateFlow()


    private val _newsResult = MutableStateFlow(emptyList<News>())
    val newsResult = _newsResult.asStateFlow()

    fun search(keyword: String) {
        if (keyword.isNotBlank()) {
            _keyword.value = keyword
            loadResult()
        }
    }

    fun loadResult() {
        viewModelScope.launch {
            TODO("Get data using NetworkManager")
        }
    }
}