package com.leeseungyun1020.searcher.utilities

import android.content.Context
import android.content.Intent
import com.leeseungyun1020.searcher.ui.LoginActivity

fun Context.start() {
    startActivity(Intent(this, LoginActivity::class.java))
}