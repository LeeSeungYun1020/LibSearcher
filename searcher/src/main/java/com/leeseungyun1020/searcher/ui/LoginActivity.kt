package com.leeseungyun1020.searcher.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leeseungyun1020.searcher.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
            /*
            when (type) {
                Type.NAVER -> {
                    NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
                        override fun onSuccess() {
                            NidOAuthLogin().callProfileApi(object :
                                NidProfileCallback<NidProfileResponse> {
                                override fun onSuccess(result: NidProfileResponse) {
                                    Toast.makeText(
                                        this@LoginActivity, "$result", Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(
                                        Intent(
                                            this@LoginActivity, SearchActivity::class.java
                                        )
                                    )
                                    finish()
                                }

                                override fun onFailure(httpStatus: Int, message: String) {
                                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Failed Naver Profile: $errorCode: $errorDescription",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e(
                                        TAG,
                                        "Failed Naver Profile: httpStatus($httpStatus) message: $message \nerrorCode($errorCode), errorDesc:$errorDescription"
                                    )
                                }

                                override fun onError(errorCode: Int, message: String) {
                                    onFailure(errorCode, message)
                                }
                            })
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                            Toast.makeText(
                                this@LoginActivity,
                                "Failed Naver Login: $errorCode: $errorDescription",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                TAG,
                                "Failed Naver Login: httpStatus($httpStatus) message: $message \nerrorCode($errorCode), errorDesc:$errorDescription"
                            )
                        }

                        override fun onError(errorCode: Int, message: String) {
                            onFailure(errorCode, message)
                        }
                    })
                }
                Type.KAKAO -> {
                    Log.d(TAG, "Did you check key hash?: ${Utility.getKeyHash(this)}")
                    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                        if (error != null) {
                            Log.e(TAG, "Failed Kakao Login: $error")
                        } else if (token != null) {
                            startActivity(Intent(this, SearchActivity::class.java))
                            finish()
                        }
                    }

                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                            if (error != null) {
                                // 로그인 취소
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    Log.e(TAG, "Failed Kakao Login: $error")
                                    return@loginWithKakaoTalk
                                }

                                // 카카오계정으로 로그인 시도
                                UserApiClient.instance.loginWithKakaoAccount(
                                    this, callback = callback
                                )
                            } else if (token != null) {
                                startActivity(Intent(this, SearchActivity::class.java))
                                finish()
                            }
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }
                }
                null -> {
                    Log.e(TAG, "LoginActivity: Undefined login type progress")
                    finish()
                }
            }
             */
        }
    }
}