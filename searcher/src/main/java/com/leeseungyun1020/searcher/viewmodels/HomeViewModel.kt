package com.leeseungyun1020.searcher.viewmodels

import androidx.lifecycle.ViewModel
import com.leeseungyun1020.searcher.utilities.HomeFragments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _location = MutableStateFlow(HomeFragments.LOGIN)
    val location = _location.asStateFlow()

    fun navigateTo(destination: HomeFragments) {
        _location.value = destination
    }
}