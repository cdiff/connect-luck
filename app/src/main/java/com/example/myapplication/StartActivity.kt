package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.data.api.AuthApi
import com.example.data.dto.LoginRequest
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.ActivityStartBinding
import com.example.myapplication.view.auth.AuthActivity
import com.example.myapplication.view.event.EventActivity
import com.example.myapplication.view.home.HomeActivity

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    private lateinit var intent: Intent

    @Inject
    lateinit var authApi: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = MySharedPreferences.getUserEmail(this)
        val password = MySharedPreferences.getUserPassword(this)

        // 기기에 저장된 아이디와 비밀번호로 자동 로그인 시도 후 화면 전환
        lifecycleScope.launch {
            if (email.isNotBlank() && password.isNotBlank()) {
                val response = authApi.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    Toast.makeText(this@StartActivity, "자동 로그인 성공", Toast.LENGTH_SHORT).show()
                    MySharedPreferences.setToken(this@StartActivity, response.body()!!.token!!)
                } else {
                    Toast.makeText(this@StartActivity, "로그인 정보가 만료되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    MySharedPreferences.clearUserInfo(this@StartActivity)
                }
            }
            changeActivity()
        }
    }

    /**
     * 로그인 정보에 따라 화면 전환
     */
    private fun changeActivity() {
        intent = if (MySharedPreferences.getUserEmail(this).isBlank()) {
            Intent(this, AuthActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        this.finish()
    }
}