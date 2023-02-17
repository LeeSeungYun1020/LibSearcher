package com.leeseungyun1020.searcher.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.searcher.databinding.ActivitySearchBinding
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SearchViewModel by viewModels()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))

    }

    private fun checkMetaData() {
        val metaData =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        when (metaData.getString("com.leeseungyun1020.searcher.sdk.type")?.lowercase()) {
            "naver" -> {
                val id = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.id")
                val pw = metaData.getString("com.leeseungyun1020.searcher.sdk.naver.pw")
                if (id.isNullOrEmpty() || pw.isNullOrEmpty()) {
                    Log.e(TAG, "checkMetaData: Input meta-data: naver id/pw")
                    finish()
                } else {
                    NetworkManager.init(Type.NAVER, id, pw)
                }
            }
            "daum", "kakao" -> {
                val key = metaData.getString("com.leeseungyun1020.searcher.sdk.kakao.key")
                if (key.isNullOrEmpty()) {
                    Log.e(TAG, "checkMetaData: Input meta-data: kakao key")
                    finish()
                } else {
                    NetworkManager.init(Type.KAKAO, key)
                }
            }
            else -> {
                Log.e(TAG, "checkMetaData: Input meta-data: type")
                finish()
            }
        }
    }
}