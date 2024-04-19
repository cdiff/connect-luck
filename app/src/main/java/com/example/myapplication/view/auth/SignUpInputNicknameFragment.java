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
import com.example.myapplication.databinding.FragmentSignUpInputNicknameBinding;
import com.example.myapplication.viewmodel.SignUpViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class SignUpInputNicknameFragment extends Fragment implements Validator.ValidationListener {
    private FragmentSignUpInputNicknameBinding binding; // 데이터 바인딩 객체

    SignUpViewModel signUpViewModel;
    @Length(min = 2, max = 100, message = "최소2")//길이
    @NotEmpty(message = "입력해주세용") //필수입력
    EditText nickname;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 데이터 바인딩 초기화
        binding = FragmentSignUpInputNicknameBinding.inflate(inflater, container, false);

        nickname = binding.inputNickname;

        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        binding.inputNickname.addTextChangedListener(new TextWatcher() {
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

            }
        });


        binding.nextButton.setOnClickListener(v -> {
            signUpViewModel.setName(binding.inputNickname.getText().toString());
            Navigation.findNavController(v).navigate(R.id.action_signUpInputNicknameFragment_to_signUpInputPhoneNumberFragment);
        });

        return binding.getRoot();
    }

    @Override
    public void onValidationSucceeded() {
        binding.nextButton.setBackgroundResource(R.drawable.btn_black);
        binding.nextButton.setEnabled(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        binding.nextButton.setBackgroundResource(R.drawable.btn_gray);
        binding.nextButton.setEnabled(false);
    }
}