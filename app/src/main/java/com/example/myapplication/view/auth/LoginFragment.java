package com.example.myapplication.view.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.data.api.AuthApi;
import com.example.data.dto.LoginRequest;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLoginBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    @Inject
    AuthApi authApi; //Retrofit을 사용하여 서버에 인증 관련 요청 보내는 데 사용

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.login.setOnClickListener(v -> {
            LoginRequest loginRequest = new LoginRequest(binding.emailEditText.getText().toString(), binding.passwordEditText.getText().toString());
            Log.i("LoginFragment", loginRequest.toString());
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "로그인 시도중", Toast.LENGTH_SHORT).show();

                try {
                    Call<String> call = authApi.login(loginRequest);
                    call.enqueue(new retrofit2.Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.isSuccessful()) {
                                String jwtToken = response.body();
                                if (jwtToken != null) {
                                    Toast.makeText(getActivity(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Log.d("LoginFragment", jwtToken.toString());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                            Log.e("LoginFragment", t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.e("LoginFragment", e.getMessage());
                }
            });
        });

        binding.signup.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_signUpInputEmailFragment2);
        });

        binding.emailFind.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_emailFindFragment);
        });

        return binding.getRoot();
    }

}