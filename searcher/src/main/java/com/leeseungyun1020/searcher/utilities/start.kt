package com.leeseungyun1020.searcher.utilities

import android.content.Context
import android.content.Intent
import com.leeseungyun1020.searcher.HomeActivity

fun Context.start(type: Type, clientId: String, clientPw: String) {
    startActivity(
        Intent(this, HomeActivity::class.java)
            .putExtra(ExtraNames.Home.type, type)
            .putExtra(ExtraNames.Home.id, clientId)
            .putExtra(ExtraNames.Home.pw, clientPw)
    )
}