package com.example.data.api;

import com.example.data.dto.EmailResponse;
import com.example.data.dto.IdCheckResponse;
import com.example.data.dto.LoginRequest;
import com.example.data.dto.SignUpRequest;
import com.example.data.dto.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    /**
     * 회원가입
     *
     * @param signUpRequest 회원가입 요청 정보
     * @return String 회원 JWT 토큰
     */
    @POST("/api/auth/signup")
    Call<TokenResponse> signup(
            @Body SignUpRequest signUpRequest
    );

    /**
     * 로그인
     *
     * @param loginRequest 로그인 요청 정보
     * @return String 회원 JWT 토큰
     */
    @POST("/api/auth/login")
    Call<TokenResponse> login(
            @Body LoginRequest loginRequest
    );

    @POST("/api/auth/findEmailByPhone")
    Call<EmailResponse> findEmailByPhone(
            @Query("phone") String phone
    );

    @POST("/api/auth/email-check")
    Call<IdCheckResponse> emailCheck(
            @Query("email") String email
    );

}
