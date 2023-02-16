package com.leeseungyun1020.searcher.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leeseungyun1020.searcher.databinding.ActivitySearchBinding
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SearchViewModel by viewModels()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // viewModel.var.collect
            }
        }
    }
}