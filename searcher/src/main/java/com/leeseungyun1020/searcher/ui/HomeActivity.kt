package com.leeseungyun1020.searcher.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leeseungyun1020.searcher.R
import com.leeseungyun1020.searcher.databinding.ActivityHomeBinding
import com.leeseungyun1020.searcher.utilities.HomeFragments
import com.leeseungyun1020.searcher.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: HomeViewModel by viewModels()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.location.collect {
                    navigateTo(it)
                }
            }
        }
    }

    private fun navigateTo(destination: HomeFragments) {
        supportFragmentManager.commit {
            when (destination) {
                HomeFragments.LOGIN -> {
                    replace<LoginFragment>(R.id.fragment_container_view)
                }
                HomeFragments.SEARCH -> {
                    replace<SearchFragment>(R.id.fragment_container_view)
                }
            }
        }
    }
}