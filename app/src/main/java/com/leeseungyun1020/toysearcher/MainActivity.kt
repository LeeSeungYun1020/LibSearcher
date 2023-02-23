package com.leeseungyun1020.toysearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.searcher.utilities.startLogin
import com.leeseungyun1020.toysearcher.databinding.ActivityMainBinding

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