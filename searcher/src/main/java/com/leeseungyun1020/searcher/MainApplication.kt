package com.leeseungyun1020.searcher

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.kakao.sdk.common.KakaoSdk
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Type
import com.navercorp.nid.NaverIdLoginSDK

class MainApplication : Application() {
    private val _supportTypes: MutableSet<Type> = mutableSetOf()
    val supportTypes = _supportTypes.toSet()

    override fun onCreate() {
        super.onCreate()
        applyMetaData()
    }

    private fun applyMetaData() {
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
        val id = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.id")
        val pw = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.pw")
        val app = metaData.getString("com.leeseungyun1020.searcher.sdk.kakao.app")
        val api = metaData.getString("com.leeseungyun1020.searcher.sdk.kakao.api")
        if (!id.isNullOrEmpty() && !pw.isNullOrEmpty()) {
            _supportTypes += Type.NAVER
            NaverIdLoginSDK.initialize(
                this,
                id,
                pw,
                applicationInfo.loadLabel(packageManager).toString()
            )
            NetworkManager.init(id, pw)
        }
        if (!app.isNullOrEmpty() && !api.isNullOrEmpty()) {
            _supportTypes += Type.KAKAO
            KakaoSdk.init(applicationContext, app)
            NetworkManager.init(api)
        }
    }
}