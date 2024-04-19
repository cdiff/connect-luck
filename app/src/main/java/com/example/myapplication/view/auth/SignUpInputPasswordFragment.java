package com.example.myapplication.view.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignUpInputPasswordBinding;
import com.example.myapplication.viewmodel.AuthViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpInputPasswordFragment extends Fragment implements Validator.ValidationListener {

    FragmentSignUpInputPasswordBinding binding;
    AuthViewModel authViewModel;

    @Length(min = 8, max = 100, message = "최소8")//길이
    @NotEmpty(message = "입력해주세용") //필수입력
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "비번은 숫자 영문대문자 특수 문자를 조합하여 입력 ")
    EditText inputpassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 뷰 인플레이트
        binding = FragmentSignUpInputPasswordBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.setViewModel(authViewModel);

        inputpassword = binding.password;

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        binding.password.addTextChangedListener(new TextWatcher() {
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


        binding.passwordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validatePasswordFields();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordFields();

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePasswordFields();

            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.prg1.setProgress(100);
                Navigation.findNavController(v).navigate(R.id.action_signUpInputPasswordFragment_to_signUpInputNicknameFragment);
                Log.d("SignUpInputPasswordFragment", "email: " + authViewModel.getEmail().getValue());
                Log.d("SignUpInputPasswordFragment", "password: " + authViewModel.getPassword().getValue());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        binding.nextButton.setBackgroundResource(R.drawable.btn_gray);
        binding.nextButton.setEnabled(false);
    }

    private void validatePasswordFields() {
        String password = binding.password.getText().toString();
        String passwordCheck = binding.passwordCheck.getText().toString();

        if (password.equals(passwordCheck)) {
            // 비밀번호 일치 시 버튼 활성화
            binding.nextButton.setBackgroundResource(R.drawable.btn_black);
            binding.nextButton.setEnabled(true);
        } else {
            // 비밀번호 불일치 시 버튼 비활성화
            binding.nextButton.setBackgroundResource(R.drawable.btn_gray);
            binding.nextButton.setEnabled(false);
        }
    }


}
