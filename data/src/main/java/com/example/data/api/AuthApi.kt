package com.example.data.api

import com.example.data.dto.EmailResponse
import com.example.data.dto.IdCheckResponse
import com.example.data.dto.LoginRequest
import com.example.data.dto.SignUpRequest
import com.example.data.dto.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<TokenResponse>

    @POST("/api/auth/signup")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<TokenResponse>

    @POST("/api/auth/findEmailByPhone")
    suspend fun findEmailByPhone(
        @Query("phone") phone: String
    ): Response<EmailResponse>

    @POST("/api/auth/email-check")
    suspend fun emailDuplicateCheck(
        @Query("email") email: String
    ): Response<IdCheckResponse>
}