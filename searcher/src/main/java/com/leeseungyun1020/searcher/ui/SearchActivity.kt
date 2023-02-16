package com.leeseungyun1020.searcher.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leeseungyun1020.searcher.databinding.ActivitySearchBinding
import com.leeseungyun1020.searcher.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SearchViewModel by viewModels()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.searchTab.imageTabButton.setOnClickListener {
            Toast.makeText(this, "Image", Toast.LENGTH_SHORT).show()
        }
        binding.searchTab.newsTabButton.setOnClickListener {
            Toast.makeText(this, "News", Toast.LENGTH_SHORT).show()
        }
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.location.collect {

                }
            }
        }
    }
}