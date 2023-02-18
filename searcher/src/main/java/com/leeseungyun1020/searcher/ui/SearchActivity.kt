package com.leeseungyun1020.searcher.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leeseungyun1020.searcher.R
import com.leeseungyun1020.searcher.databinding.ActivitySearchBinding
import com.leeseungyun1020.searcher.network.NetworkManager
import com.leeseungyun1020.searcher.utilities.ResultCategory
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.utils.L
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SearchViewModel by viewModels()

        val resultFragments = ResultCategory.values().associateWith {
            supportFragmentManager.findFragmentByTag(it.name) ?: ResultFragment.newInstance(it)
        }

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
        L.writeLogs(false)

        checkMetaData()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.searchTab.newsTabButton.setOnClickListener {
            viewModel.onCategoryButtonClicked(ResultCategory.NEWS)
        }
        binding.searchTab.imageTabButton.setOnClickListener {
            viewModel.onCategoryButtonClicked(ResultCategory.IMAGE)
        }
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.location.collect {
                    supportFragmentManager.commit {
                        replace(R.id.result_container,
                            resultFragments.getOrElse(it) { ResultFragment.newInstance(it) },
                            it.name
                        )
                        setReorderingAllowed(true)
                        addToBackStack(it.name)
                    }
                }
            }
        }
    }

    private fun checkMetaData() {
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