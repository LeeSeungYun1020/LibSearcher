package com.leeseungyun1020.searcher.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.leeseungyun1020.searcher.databinding.ActivityLoginBinding
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import com.leeseungyun1020.searcher.utilities.checkMetaData
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var type: Type? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkMetaData(
            packageManager, packageName,
            onSuccess = { metaData ->
                type = metaData.type
                when (metaData.type) {
                    Type.NAVER -> NaverIdLoginSDK.initialize(
                        this,
                        metaData.id,
                        metaData.pw,
                        applicationInfo.loadLabel(packageManager).toString()
                    )
                    Type.KAKAO -> KakaoSdk.init(applicationContext, metaData.id)
                }
            },
            onError = {
                Log.e(TAG, "LoginActivity checkMetaData: $it")
                finish()
            },
        )
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loginButton.setOnClickListener {
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
        }
        setContentView(binding.root)
    }
}