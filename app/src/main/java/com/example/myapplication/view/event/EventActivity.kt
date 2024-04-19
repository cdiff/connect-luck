package com.example.myapplication.view.event

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.ActivityEventBinding
import com.example.myapplication.view.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventActivity : AppCompatActivity() {

    lateinit var binding: ActivityEventBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userEmail.text = MySharedPreferences.getUserEmail(this)
        binding.userPassword.text = MySharedPreferences.getUserPassword(this)
        binding.userJwt.text = MySharedPreferences.getToken(this)

        binding.logoutButton.setOnClickListener {
            MySharedPreferences.clearUserInfo(this)
            val intent: Intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}