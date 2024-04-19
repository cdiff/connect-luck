package com.example.myapplication.common

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences를 사용하여 사용자 정보를 저장하는 싱글톤 객체
 */
object MySharedPreferences {
    private const val USER_INFO = "user_info"
    private const val USER_EMAIL = "user_email"
    private const val USER_PASSWORD = "user_password"
    private const val JWT_TOKEN = "jwt_token"

    /**
     * 사용자 정보를 저장하는 함수
     * @param context Context
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     */
    fun setUserInfo(context: Context, email: String, password: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_PASSWORD, password)
        editor.apply()
    }

    /**
     * JWT 토큰을 저장하는 함수
     * @param context Context
     * @param token JWT 토큰
     */
    fun setToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(JWT_TOKEN, token)
        editor.apply()
    }

    /**
     * JWT 토큰을 반환하는 함수
     * @param context Context
     * @return JWT 토큰
     */
    fun getToken(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(JWT_TOKEN, "")!!
    }

    /**
     * 사용자 이메일을 반환하는 함수
     * @param context Context
     * @return 사용자 이메일
     */
    fun getUserEmail(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_EMAIL, "")!!
    }

    /**
     * 사용자 비밀번호를 반환하는 함수
     * @param context Context
     * @return 사용자 비밀번호
     */
    fun getUserPassword(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_PASSWORD, "")!!
    }

    /**
     * 사용자 정보를 삭제(로그아웃)하는 함수
     * @param context Context
     */
    fun clearUserInfo(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}