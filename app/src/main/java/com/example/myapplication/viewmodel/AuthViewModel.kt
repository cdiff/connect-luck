package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.api.AuthApi
import com.example.data.api.ImageApi
import com.example.data.dto.ImageUrlResponse
import com.example.data.dto.LoginRequest
import com.example.data.dto.SignUpRequest
import com.example.data.dto.TokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    // 로그인 이메일, 비밀번호
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    // 회원가입 추가 정보
    val name = MutableLiveData<String>("")
    val phoneNumber = MutableLiveData<String>("")
    val profileImageUrl = MutableLiveData<String>("")


    fun getLoginRequest(): LoginRequest {
        return LoginRequest(
            email = email.value!!,
            password = password.value!!
        )
    }
}