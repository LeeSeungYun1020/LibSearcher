package com.leeseungyun1020.searcher.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.searcher.MainApplication
import com.leeseungyun1020.searcher.R
import com.leeseungyun1020.searcher.databinding.ActivityLoginBinding
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import com.leeseungyun1020.searcher.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val types = (application as MainApplication).supportLoginTypes
            if (types.size == 1) {
                login(types.first())
            } else {
                TypeSelectDialogFragment(
                    types = types,
                    titleId = R.string.login,
                    emptyMessageId = R.string.login_unsupported_error,
                    onSelect = this::login
                ).show(supportFragmentManager, "login")
            }
        }
    }

    private fun login(type: Type) {
        viewModel.login(
            type = type,
            context = this,
            onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                startActivity(Intent(this, SearchActivity::class.java))
                finish()
            },
            onFailure = { message ->
                Log.e(TAG, "LoginActivity: failed $type login")
                Log.e(TAG, message)
            }
        )
    }
}