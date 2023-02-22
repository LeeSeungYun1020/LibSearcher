package com.leeseungyun1020.searcher.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.leeseungyun1020.searcher.utilities.TAG
import com.leeseungyun1020.searcher.utilities.Type
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginViewModel : ViewModel() {
    fun login(
        type: Type,
        context: Context,
        onSuccess: (message: String) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        when (type) {
            Type.NAVER -> naverLogin(context, onSuccess, onFailure)
            Type.KAKAO -> kakaoLogin(context, onSuccess, onFailure)
        }
    }

    private fun naverLogin(
        context: Context,
        onSuccess: (message: String) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(object :
                    NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        onSuccess(result.toString())
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        onFailure("Failed Naver Profile: httpStatus($httpStatus) message: $message \nerrorCode($errorCode), errorDesc:$errorDescription")
                    }

                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }
                })
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                onFailure("Failed Naver Login: httpStatus($httpStatus) message: $message \nerrorCode($errorCode), errorDesc:$errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    private fun kakaoLogin(
        context: Context,
        onSuccess: (message: String) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        Log.d(TAG, "Did you check key hash?: ${Utility.getKeyHash(context)}")

        fun loadUserInfo() {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    onFailure("Failed Kakao profile: $error")
                } else if (user != null) {
                    onSuccess("${user.kakaoAccount?.profile?.nickname}")
                }
            }
        }

        fun callback(token: OAuthToken?, error: Throwable?) {
            if (error != null) {
                onFailure("Failed Kakao Login: $error")
            } else if (token != null) {
                loadUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    // 로그인 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        onFailure("Failed Kakao Login: $error")
                        return@loginWithKakaoTalk
                    }

                    // 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        context, callback = ::callback
                    )
                } else if (token != null) {
                    loadUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context, callback = ::callback
            )
        }
    }
}