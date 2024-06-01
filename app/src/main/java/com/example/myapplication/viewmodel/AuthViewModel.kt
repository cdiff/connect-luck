package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.AuthApi
import com.example.data.dto.LoginRequest
import com.example.data.dto.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val phoneNumber = MutableLiveData<String>("")
    val profileImageUrl = MutableLiveData<String>("")

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess

    fun signUp() {
        val signUpRequest = SignUpRequest(
            email = email.value!!,
            password = password.value!!,
            name = name.value!!,
            phoneNumber = phoneNumber.value!!,
        )

        viewModelScope.launch {
            val response = authApi.signUp(signUpRequest)
            _signUpSuccess.postValue(response.isSuccessful)
        }
    }

    fun getLoginRequest(): LoginRequest {
        return LoginRequest(
            email = email.value!!,
            password = password.value!!
        )
    }
}


