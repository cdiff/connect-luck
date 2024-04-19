package com.example.myapplication.view.signupfragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.data.api.AuthApi;
import com.example.data.dto.SignUpRequest;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignUpInputPhonenumberBinding;
import com.example.myapplication.viewmodel.SignUpViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SignUpInputPhoneNumberFragment extends Fragment {

    @Inject
    AuthApi authApi;

    private FragmentSignUpInputPhonenumberBinding binding;
    private SignUpViewModel signUpViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpInputPhonenumberBinding.inflate(inflater, container, false);

        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        binding.inputPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateNextButtonState();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNextButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateNextButtonState();
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            String email = signUpViewModel.getEmail().getValue(); // 이메일 값 가져오기
            String password = signUpViewModel.getPassword().getValue(); // 비밀번호 값 가져오기
            String name = signUpViewModel.getName().getValue(); // 이름 값 가져오기
            String phoneNumber = binding.inputPhonenumber.getText().toString(); // 전화번호 값 가져오기

            if (email != null && password != null && name != null) {
                // 모든 필수 값이 null이 아닌 경우에만 SignUpRequest 객체 생성
                SignUpRequest signUpRequest = new SignUpRequest(email, password, name, phoneNumber);
                getActivity().runOnUiThread(() -> {
                    Log.i("SignUpInputPhoneNumberFragment", "회원가입 요청: " + signUpRequest.toString());
                    try {
                        Call<String> call = authApi.signup(signUpRequest);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.isSuccessful()) {
                                    String jwtToken = response.body();
                                    if (jwtToken != null) {
                                        Toast.makeText(getActivity(), "로인그 성공", Toast.LENGTH_SHORT).show();
                                        Log.e("LoginFragment", jwtToken.toString());

                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                System.out.println("실패");
                                Log.e("LoginFragment", t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("실패");
                    }
                });
            } else {
                // 필수 값 중 하나라도 null인 경우 처리
                Toast.makeText(getActivity(), "필수 정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                System.out.println(email);
                System.out.println(name);
                System.out.println(password);
                System.out.println(phoneNumber);
            }
        });

        return binding.getRoot();
    }

    private void updateNextButtonState() {
        String phoneNumber = binding.inputPhonenumber.getText().toString().trim();

        // 전화번호 유효성 검사 (여기서는 간단하게 길이를 확인)
        boolean isValid = !phoneNumber.isEmpty() && phoneNumber.length() >= 10; // 예시: 최소 10자리 이상

        binding.nextButton.setEnabled(isValid);
        binding.nextButton.setBackgroundResource(isValid ? R.drawable.btn_black : R.drawable.btn_gray);
    }

}
