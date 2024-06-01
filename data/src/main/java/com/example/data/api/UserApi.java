package com.example.data.api;

import com.example.data.dto.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    /**
     * 사용자에게 권한을 추가합니다
     *
     * @param role  요청하려는 권한
     *              UserRole enum 값 중 하나를 사용합니다
     */
    @POST("/api/user/add-role")
    Call<User> addRole(
            @Header("Authorization") String token,
            @Query("role") String role
    );


    /**
     * 현재 로그인한 사용자의 정보를 가져옵니다.
     *
     * @param token 사용자 JWT 토큰
     */
    @GET("api/user")
    Call<User> getMe(
            @Header("Authorization") String token
    );
}