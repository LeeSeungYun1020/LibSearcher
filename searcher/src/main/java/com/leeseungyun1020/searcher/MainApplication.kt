package com.leeseungyun1020.searcher

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.kakao.sdk.common.KakaoSdk
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.Type
import com.navercorp.nid.NaverIdLoginSDK

class MainApplication : Application() {
    private val _supportLoginTypes: MutableSet<Type> = mutableSetOf()
    val supportLoginTypes
        get() = _supportLoginTypes.toSet()

    private val _supportSearchTypes: MutableSet<Type> = mutableSetOf()
    val supportSearchTypes
        get() = _supportSearchTypes.toSet()

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
            _supportLoginTypes += Type.NAVER
            _supportSearchTypes += Type.NAVER
            NaverIdLoginSDK.initialize(
                this,
                id,
                pw,
                applicationInfo.loadLabel(packageManager).toString()
            )
            NetworkManager.init(id, pw)
        }
        if (!app.isNullOrEmpty()) {
            _supportLoginTypes += Type.KAKAO
            KakaoSdk.init(applicationContext, app)
        }
        if (!api.isNullOrEmpty()) {
            _supportSearchTypes += Type.KAKAO
            NetworkManager.init(api)
        }
    }
}