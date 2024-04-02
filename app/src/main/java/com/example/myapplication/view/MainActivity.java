package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.view.auth.AuthActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
// TODO: Activity Rename (MainActivity -> LoadingActivity)
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: 로그인 여부 확인 후 로그인이 되어있지 않다면 로그인 화면으로 이동
        if (true) {
            intent = new Intent(this, AuthActivity.class);
        } else {
            intent = new Intent(this, AuthActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
