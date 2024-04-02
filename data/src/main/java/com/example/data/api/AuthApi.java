package com.example.data.api;

import com.example.data.dto.LoginRequest;
import com.example.data.dto.SignUpRequest;
import com.example.data.dto.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/api/signup")
    Call<User> signup(
            @Body SignUpRequest signUpRequest
    );

    @POST("/api/login")
    Call<User> login(
            @Body LoginRequest loginRequest
    );

    @POST("/api/findEmailByPhone")
    Call<ResponseBody> findEmailByPhone(
            @Query("phone") String phone
    );
}
