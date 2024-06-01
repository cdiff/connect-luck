package com.example.myapplication.view.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.data.api.AuthApi
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSignUpInputEmailBinding
import com.example.myapplication.viewmodel.AuthViewModel
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpInputEmailFragment : Fragment(), Validator.ValidationListener {

    @Inject
    lateinit var authApi: AuthApi

    private lateinit var binding: FragmentSignUpInputEmailBinding
    private lateinit var authViewModel: AuthViewModel

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    lateinit var inputEmail: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpInputEmailBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.viewModel = authViewModel
        inputEmail = binding.inputEmail


        val validator = Validator(this)
        validator.setValidationListener(this)
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validator.validate()
            }
        })

        binding.nextButton.setOnClickListener {
            lifecycleScope.launch {
                val response = authApi.emailDuplicateCheck(authViewModel.email.value!!)
                if (response.isSuccessful) {
                    if (response.body()!!.isAvailable) {
                        Toast.makeText(context, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                        authViewModel.email.value = binding.inputEmail.text.toString().trim()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_signUpInputEmailFragment2_to_signUpInputPasswordFragment)
                    } else {
                        Toast.makeText(context, "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "이메일 중복 확인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        binding.nextButton.setBackgroundResource(R.drawable.btn_gray)
        binding.nextButton.isEnabled = false
    }

    override fun onValidationSucceeded() {
        binding.nextButton.setBackgroundResource(R.drawable.btn_black)
        binding.nextButton.isEnabled = true
    }
}
