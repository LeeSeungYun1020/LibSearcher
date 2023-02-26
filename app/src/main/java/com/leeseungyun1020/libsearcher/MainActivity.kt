package com.leeseungyun1020.libsearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.libsearcher.databinding.ActivityMainBinding
import com.leeseungyun1020.searcher.utilities.startLogin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
            startLogin()
            //startSearch()
            finish()
        }
    }
}