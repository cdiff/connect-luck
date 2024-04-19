package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityStartBinding
import com.example.myapplication.view.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity @Inject constructor(

) : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: 로그인 여부 확인 후 로그인이 되어있지 않다면 로그인 화면으로 이동

        if (true) {
            intent = Intent(this, AuthActivity::class.java)

        } else {
            intent = Intent(this, AuthActivity::class.java)
        }

        startActivity(intent)
        this.finish()
    }
}