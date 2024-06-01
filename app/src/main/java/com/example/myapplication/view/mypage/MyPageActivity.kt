package com.example.myapplication.view.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity  : AppCompatActivity() {
    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}