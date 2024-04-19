package com.example.myapplication.view.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignUpInputEmailBinding;
import com.example.myapplication.viewmodel.SignUpViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

import java.util.List;


public class SignUpInputEmailFragment extends Fragment implements Validator.ValidationListener {
    FragmentSignUpInputEmailBinding binding;
    SignUpViewModel signUpViewModel;

    @Email(message = "이메일 형식으로")
    EditText inputEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 뷰 인플레이트
        binding = FragmentSignUpInputEmailBinding.inflate(inflater, container, false);

        inputEmail = binding.inputEmail;


        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);


        Validator validator = new Validator(this);
        validator.setValidationListener(this);


        binding.inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validator.validate();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validator.validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate();
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.prg.setProgress(0);

                signUpViewModel.setEmail(binding.inputEmail.getText().toString());// 사용자가 입력한 이메일을 뷰모델에 저장

                Navigation.findNavController(v).navigate(R.id.action_signUpInputEmailFragment2_to_signUpInputPasswordFragment);
            }
        });

        return binding.getRoot();

    }

    //유효성 검사 통과시 호출
    @Override
    public void onValidationSucceeded() {
        // 이메일 형식이 유효할 때 버튼 배경 변경 및 활성화
        binding.nextButton.setBackgroundResource(R.drawable.btn_black);
        binding.nextButton.setEnabled(true);
    }

    //유효성 검사 오류시 호출
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        // 이메일 형식이 유효하지 않을 때 버튼 배경 원래대로 변경 및 비활성화
        binding.nextButton.setBackgroundResource(R.drawable.btn_gray);
        binding.nextButton.setEnabled(false);
    }

}
