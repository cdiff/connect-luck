package com.example.myapplication;

import com.example.data.api.AuthApi;
import com.example.data.dto.LoginRequest;
import com.example.data.dto.User;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2Test {

    Retrofit retrofit;
    AuthApi loginApi;

    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://connect-luck.store")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginApi = retrofit.create(AuthApi.class);
    }

    @Test
    public void testRetrofit2() throws IOException {
        LoginRequest loginRequest = new LoginRequest("test4@test.com", "test4");
        Response<User> user = loginApi.login(loginRequest).execute();

        System.out.println(user.body());
        System.out.println(user.code());


    }
}
