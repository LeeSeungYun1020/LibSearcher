package com.leeseungyun1020.searcher.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.searcher.databinding.ActivityLoginBinding
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.checkMetaData

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkMetaData(
            packageManager, packageName,
            onSuccess = { metaData ->
                
            },
            onError = {
                Log.e(TAG, "LoginActivity checkMetaData: $it")
            },
        )
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }
        setContentView(binding.root)
    }
}