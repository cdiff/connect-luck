package com.example.myapplication.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentMypageBinding
import com.example.myapplication.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set user information
        setUserInformation()

        // Populate list view
        populateListView()

        // Observe roleAdded LiveData
        viewModel.roleAdded.observe(viewLifecycleOwner) { isAdded ->
            if (isAdded) {
                // Update UI here or perform any necessary actions after role is added
                Toast.makeText(requireContext(), "푸드트럭 매니저 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.mytruck.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_mytruckFragment)
        }
        return view
    }

    private fun setUserInformation() {
        val token = MySharedPreferences.getToken(requireContext())
        if (token.isNotEmpty()) {
            viewModel.getUserInfo(token) { user ->
                user?.let {
                    binding.textView13.text = user.email
                    binding.textView11.text = user.name
                }
            }
        }
    }

    private fun populateListView() {
        val data = arrayOf("푸드트럭 등록", "푸드 트럭 메뉴 등록", "푸드트럭 매니저 권한 설정")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = data[position]
            when (selectedItem) {
                "푸드트럭 등록" -> {
                    findNavController().navigate(R.id.action_myPageFragment_to_foodTruckCreateFragment)
                }
                "푸드 트럭 메뉴 등록" -> {
                    Toast.makeText(requireContext(), "메뉴를 등록할 푸드트럭을 선택해주세요", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_myPageFragment_to_mytruckFragment)

                }
                "푸드트럭 매니저 권한 설정" -> {
                    val token = MySharedPreferences.getToken(requireContext())
                    if (token.isNotEmpty()) {
                        viewModel.addFoodTruckManagerRole(token, "FOOD_TRUCK_MANAGER")
                    } else {
                        // 토큰이 없는 경우에 대한 처리
                        Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                // Additional items can be handled here
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
