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
import com.example.myapplication.di.RetrofitClient
import com.example.myapplication.viewmodel.AuthViewModel
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.Validator.ValidationListener
import com.mobsandgeeks.saripaar.annotation.Email
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpInputEmailFragment : Fragment(), ValidationListener {


    lateinit var binding: FragmentSignUpInputEmailBinding
    private lateinit var authViewModel: AuthViewModel

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    lateinit var inputEmail: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpInputEmailBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.viewModel = authViewModel
        inputEmail = binding.inputEmail

        // 툴바 뒤로가기 버튼 활성화
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        })

        // 이메일 입력란 유효성 검사
        val validator = Validator(this)
        validator.setValidationListener(this)
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validator.validate()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validator.validate()
            }

            override fun afterTextChanged(s: Editable?) {
                validator.validate()
            }
        })

        // 다음 버튼 클릭 시
        binding.nextButton.setOnClickListener(View.OnClickListener {
            val authApi: AuthApi = RetrofitClient.retrofit.create(AuthApi::class.java)
            lifecycleScope.launch {
                val response = authApi.emailDuplicateCheck(authViewModel.email.value!!)
                if (response.isSuccessful) {
                    if (response.body()!!.isAvailable) {
                        // 이메일 중복 확인 성공
                        Toast.makeText(context, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_signUpInputEmailFragment2_to_signUpInputPasswordFragment)
                    } else {
                        // 이메일 중복 확인 실패
                        Toast.makeText(context, "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 이메일 중복 확인 실패
                    Toast.makeText(context, "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }


    // 이메일 형식이 유효하지 않을 때 버튼 배경 원래대로 변경 및 비활성화
    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        binding.nextButton.setBackgroundResource(R.drawable.btn_gray)
        binding.nextButton.setEnabled(false)
    }

    // 이메일 형식이 유효할 때 버튼 배경 변경 및 활성화
    override fun onValidationSucceeded() {
        binding.nextButton.setBackgroundResource(R.drawable.btn_black)
        binding.nextButton.setEnabled(true)
    }
}