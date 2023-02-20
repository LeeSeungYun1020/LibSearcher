package com.leeseungyun1020.searcher.utilities

import android.content.pm.PackageManager
import android.os.Build

data class MetaData(
    val type: Type,
    val id: String,
    val pw: String
)

fun checkMetaData(
    packageManager: PackageManager,
    packageName: String,
    onSuccess: (metaData: MetaData) -> Unit,
    onError: (message: String) -> Unit
) {
    val metaData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        ).metaData
    } else {
        packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        ).metaData
    }
    when (metaData.getString("com.leeseungyun1020.searcher.sdk.type")?.lowercase()) {
        "naver" -> {
            val id = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.id")
            val pw = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.pw")
            if (id.isNullOrEmpty() || pw.isNullOrEmpty()) {
                onError("Naver id/pw")
            } else {
                onSuccess(MetaData(Type.NAVER, id, pw))
            }
        }
        "daum", "kakao" -> {
            val app = metaData.getString("com.leeseungyun1020.searcher.sdk.kakao.app")
            val api = metaData.getString("com.leeseungyun1020.searcher.sdk.kakao.api")
            if (app.isNullOrEmpty() || api.isNullOrEmpty()) {
                onError("Kakao app/api key")
            } else {
                onSuccess(MetaData(Type.KAKAO, app, api))
            }
        }
        else -> {
            onError("Undefined type")
        }
    }
}