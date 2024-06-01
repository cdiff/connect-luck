package com.example.myapplication.view.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSignUpInputPhonenumberBinding;
import com.example.myapplication.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpInputPhoneNumberFragment extends Fragment {

    private FragmentSignUpInputPhonenumberBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpInputPhonenumberBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.setViewModel(authViewModel);
        binding.setLifecycleOwner(this); // Bind the fragment's lifecycle

        binding.inputPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateNextButtonState();
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            String phoneNumber = binding.inputPhonenumber.getText().toString().trim();
            authViewModel.getPhoneNumber().setValue(phoneNumber); // Update ViewModel

            authViewModel.signUp(); // Trigger the sign-up process

            authViewModel.getSignUpSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(requireContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.action_signUpInputPhoneNumberFragment_to_loginFragment);
                } else {
                    Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return binding.getRoot();
    }

    private void updateNextButtonState() {
        String phoneNumber = binding.inputPhonenumber.getText().toString().trim();
        boolean isValid = !phoneNumber.isEmpty() && phoneNumber.length() >= 10; // 예시: 최소 10자리 이상

        binding.nextButton.setEnabled(isValid);
        binding.nextButton.setBackgroundResource(isValid ? R.drawable.btn_black : R.drawable.btn_gray);
    }
}
