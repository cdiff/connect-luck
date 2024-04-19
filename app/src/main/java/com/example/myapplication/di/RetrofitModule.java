package com.example.myapplication.di;

import com.example.data.api.AuthApi;
import com.example.data.api.FoodTruckApi;
import com.example.data.api.UserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Singleton
    @Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://connect-luck.store")
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public AuthApi provideLoginApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

    @Singleton
    @Provides
    public FoodTruckApi provideFoodTruckApi(Retrofit retrofit) {
        return retrofit.create(FoodTruckApi.class);
    }

    @Singleton
    @Provides
    public UserApi provideUserApi(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

}
