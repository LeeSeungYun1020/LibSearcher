package com.leeseungyun1020.searcher.utilities

import android.content.Context
import android.content.Intent
import com.leeseungyun1020.searcher.ui.LoginActivity
import com.leeseungyun1020.searcher.ui.SearchActivity

fun Context.startLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Context.startSearch() {
    startActivity(Intent(this, SearchActivity::class.java))
}