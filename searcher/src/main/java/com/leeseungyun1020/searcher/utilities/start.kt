package com.leeseungyun1020.searcher.utilities

import android.content.Context
import android.content.Intent
import com.leeseungyun1020.searcher.HomeActivity

fun Context.start(type: Type) {
    startActivity(
        Intent(this, HomeActivity::class.java)
            .putExtra(ExtraNames.Home.type, type.name)
    )
}