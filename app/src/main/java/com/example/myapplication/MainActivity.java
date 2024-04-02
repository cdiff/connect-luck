package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.data.api.AuthApi;
import com.example.data.dto.LoginRequest;
import com.example.data.dto.User;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // inject the Retrofit LoginApi service
    @Inject
    AuthApi loginApi;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(v -> {
            // create a new thread to perform network request
            new Thread(() -> {
                try {
                    // perform the network request
                    Response<User> response = loginApi.login(new LoginRequest(binding.email.getText().toString(), binding.password.getText().toString())).execute();
                    // check if the request was successful

                    if (response.isSuccessful()) {
                        String responseBody = response.body().toString();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "로그인 성공: " + responseBody, Toast.LENGTH_SHORT).show());
                    } else {
                        // Login failed
                        // Show Toast message indicating login failure
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "정보가 없습니다", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        binding.signup.setOnClickListener(v -> {
            //        TODO: Implement the signup button
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Call<User> call = loginApi.signup(new LoginRequest(editText1.getText().toString(), editText2.getText().toString()));
//
//                call.enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "회원 가입 성공", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable throwable) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            }
//        });
        });
    }
}
