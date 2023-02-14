package com.leeseungyun1020.searcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leeseungyun1020.searcher.databinding.ActivityHomeBinding
import com.leeseungyun1020.searcher.utilities.ExtraNames
import com.leeseungyun1020.searcher.utilities.Type

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.type = Type.valueOf(intent.getStringExtra(ExtraNames.Home.type) ?: Type.values().first().name)
        binding.id = intent.getStringExtra(ExtraNames.Home.id) ?: ""
        binding.pw = intent.getStringExtra(ExtraNames.Home.pw) ?: ""
        setContentView(binding.root)
    }
}