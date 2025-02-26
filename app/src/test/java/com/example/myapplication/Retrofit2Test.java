package com.example.myapplication;

import com.example.data.dto.LoginRequest;

import org.junit.Assert;
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
        Response<String> user = loginApi.login(loginRequest).execute();

        Assert.assertNotNull(user.body());
        Assert.assertEquals(200, user.code());

    }
}
