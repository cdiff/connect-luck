package com.example.myapplication.view.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.ActivityAuthBinding;
import com.example.myapplication.viewmodel.SignUpViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;
    SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

    }
}