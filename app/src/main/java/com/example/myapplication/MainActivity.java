package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // inject the Retrofit LoginApi service
    @Inject
    LoginApi loginApi;

    EditText editText1, editText2;
    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.login);
        button2 = findViewById(R.id.signup);

        editText1 = findViewById(R.id.email);
        editText2 = findViewById(R.id.password);

        button1.setOnClickListener(v -> {
            // create a new thread to perform network request
            new Thread(() -> {
                try {
                    // perform the network request
                    Response<ResponseBody> response = loginApi.login(new LoginRequest(editText1.getText().toString(), editText2.getText().toString()))
                            .execute();
                    // check if the request was successful

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
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

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = loginApi.signup(editText1.getText().toString(), editText2.getText().toString());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "회원 가입 성공", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });



    }
}
