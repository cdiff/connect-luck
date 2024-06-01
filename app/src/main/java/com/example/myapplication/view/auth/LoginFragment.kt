package com.example.myapplication.view.auth

import android.content.Intent
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
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.view.home.HomeActivity
import com.example.myapplication.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var authApi: AuthApi

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.viewModel = authViewModel

        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                val loginRequest: LoginRequest = authViewModel.getLoginRequest()
                val response: Response<TokenResponse> = authApi.login(loginRequest)
                if (response.isSuccessful) {
                    val token: String = response.body()!!.token!!
                    Toast.makeText(context, "로그인 성공 토큰: $token", Toast.LENGTH_SHORT).show()
                    MySharedPreferences.setUserInfo(
                        requireContext(),
                        loginRequest.email,
                        loginRequest.password
                    )
                    MySharedPreferences.setToken(requireContext(), token)

                    // 로그인 성공 시 EventActivity를 시작
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signup.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragment2ToSignUpInputEmailFragment2()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.emailFind.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragment2ToEmailFindFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

        return binding.root
    }
}
