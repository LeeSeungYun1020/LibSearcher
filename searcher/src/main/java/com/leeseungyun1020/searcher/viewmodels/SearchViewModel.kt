package com.leeseungyun1020.searcher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leeseungyun1020.searcher.data.Image
import com.leeseungyun1020.searcher.data.ItemResult
import com.leeseungyun1020.searcher.data.News
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Category
import com.leeseungyun1020.searcher.utilities.Mode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    private val _location = MutableStateFlow(Location(Category.NEWS, true))
    val location = _location.asStateFlow()

    private val _imageResult = MutableStateFlow(ItemResult<Image>(emptyList(), Mode.REPLACE))
    val imageResult = _imageResult.asStateFlow()
    private val _imagePagingOptions = PagingOptions(30, 0, true)

    private val _newsResult = MutableStateFlow(ItemResult<News>(emptyList(), Mode.REPLACE))
    val newsResult = _newsResult.asStateFlow()
    private val _newsPagingOptions = PagingOptions(30, 0, true)

    private val _tabVisibility = MutableStateFlow(true)
    val tabVisibility = _tabVisibility.asStateFlow()

    fun search(keyword: String) {
        if (keyword.isNotBlank()) {
            _keyword.value = keyword
            _newsPagingOptions.reset()
            _imagePagingOptions.reset()
            loadResult()
        }
    }

    fun onCategoryButtonClicked(category: Category) {
        if (location.value.category != category) _location.value = Location(category, true)
    }

    fun onPopFromBackstack(category: Category) {
        _location.value = Location(category, false)
    }

    private fun loadResult() {
        viewModelScope.launch {
            _imagePagingOptions.lock()
            _newsPagingOptions.lock()
            NetworkManager.loadNews(keyword.value, 0, _newsPagingOptions.display, {
                _newsResult.value = ItemResult(it, Mode.REPLACE)
                if (it.size == _newsPagingOptions.display) {
                    _newsPagingOptions.unlock()
                }
            })
            NetworkManager.loadImages(keyword.value, 0, _imagePagingOptions.display, {
                _imageResult.value = ItemResult(it, Mode.REPLACE)
                if (it.size == _imagePagingOptions.display) {
                    _imagePagingOptions.unlock()
                }
            })
        }
    }

    fun loadMore(category: Category) {
        val pagingOptions = when (category) {
            Category.NEWS -> _newsPagingOptions
            Category.IMAGE -> _imagePagingOptions
        }

        viewModelScope.launch {
            if (pagingOptions.canLoading) {
                pagingOptions.lock()
                pagingOptions.nextPage()

                when (category) {
                    Category.NEWS -> {
                        NetworkManager.loadNews(
                            keyword.value,
                            pagingOptions.page,
                            pagingOptions.display,
                            {
                                if (it.isNotEmpty()) {
                                    val prev = _newsResult.value.items
                                    _newsResult.value = ItemResult(prev + it, Mode.ADD)
                                }
                                if (it.size == pagingOptions.display) {
                                    pagingOptions.unlock()
                                }
                            })
                    }
                    Category.IMAGE -> {
                        NetworkManager.loadImages(
                            keyword.value,
                            pagingOptions.page,
                            pagingOptions.display,
                            {
                                if (it.isNotEmpty()) {
                                    val prev = _imageResult.value.items
                                    _imageResult.value = ItemResult(prev + it, Mode.ADD)
                                }
                                if (it.size == pagingOptions.display) {
                                    pagingOptions.unlock()
                                }
                            })
                    }
                }
            }
        }
    }

    fun loadComplete(category: Category) {
        when (category) {
            Category.IMAGE -> _imageResult.value =
                ItemResult(_imageResult.value.items, Mode.COMPLETE)
            Category.NEWS -> _newsResult.value = ItemResult(_newsResult.value.items, Mode.COMPLETE)
        }
    }

    fun hideTab() {
        _tabVisibility.value = false
    }

    fun showTab() {
        _tabVisibility.value = true
    }

    class Location(
        val category: Category,
        val move: Boolean
    )

    private class PagingOptions(
        val display: Int,
        page: Int,
        canLoading: Boolean,
    ) {
        var page = page
            private set
        var canLoading = canLoading
            private set

        fun reset() {
            page = 0
            canLoading = true
        }

        fun lock() {
            canLoading = false
        }

        fun unlock() {
            canLoading = true
        }

        fun nextPage() {
            page += 1
        }
    }
}