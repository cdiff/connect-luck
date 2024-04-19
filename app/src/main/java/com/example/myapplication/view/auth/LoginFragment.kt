package com.example.myapplication.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.data.api.AuthApi
import com.example.data.dto.LoginRequest
import com.example.data.dto.TokenResponse
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.di.RetrofitClient
import com.example.myapplication.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor() : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.viewModel = authViewModel

        // 로그인 버튼 클릭 시
        binding.loginButton.setOnClickListener(View.OnClickListener {
            val authAPI: AuthApi = RetrofitClient.retrofit.create(AuthApi::class.java)
            lifecycleScope.launch {
                val loginRequest: LoginRequest = authViewModel.getLoginRequest()
                val response: Response<TokenResponse> = authAPI.login(loginRequest)
                if (response.isSuccessful) {
                    val token: String = response.body()!!.token!!
                    Toast.makeText(context, "로그인 성공 토큰: $token", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        });

        // 회원가입 버튼 클릭 시
        binding.signup.setOnClickListener(View.OnClickListener {
            val action = LoginFragmentDirections.actionLoginFragment2ToSignUpInputEmailFragment2()
            Navigation.findNavController(binding.root).navigate(action)
        });

        // 이메일 찾기 버튼 클릭 시
        binding.emailFind.setOnClickListener(View.OnClickListener {
            val action = LoginFragmentDirections.actionLoginFragment2ToEmailFindFragment()
            Navigation.findNavController(binding.root).navigate(action)
        });

        return binding.root
    }
}