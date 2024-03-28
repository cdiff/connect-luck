package com.example.myapplication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    @POST("/api/signup")
    Call<ResponseBody> signup(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("/api/login")
    Call<ResponseBody> login(
            @Body LoginRequest loginRequest
    );
}
