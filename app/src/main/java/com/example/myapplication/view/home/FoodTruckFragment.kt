package com.example.myapplication.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFoodTruckBinding
import com.example.myapplication.viewmodel.FoodTruckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodTruckFragment : Fragment() {

    private lateinit var binding: FragmentFoodTruckBinding
    private lateinit var foodTruckAdapter: FoodTruckAdapterList
    private val foodTruckViewModel: FoodTruckViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodTruckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)


        // 푸드트럭 어댑터 초기화
        foodTruckAdapter = FoodTruckAdapterList(requireContext(), emptyList(), nestedScrollView, R.id.foodTruckFragment)
        binding.foodTruckListView.adapter = foodTruckAdapter

        // FoodTruckViewModel에서 초기 데이터 가져와서 ListView에 표시
        foodTruckViewModel.searchFoodTruck("") // 초기 데이터 로드


        // FoodTruckViewModel에서 데이터 가져와서 ListView에 표시
        foodTruckViewModel.foodTruckList.observe(viewLifecycleOwner) { foodTruckHeaders ->
            foodTruckAdapter.updateData(foodTruckHeaders)
        }

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에 수행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 수행할 작업
                val searchText = s.toString().trim() // 사용자가 입력한 텍스트 가져오기
                foodTruckViewModel.searchFoodTruck(searchText) // 검색어로 FoodTruckViewModel에 데이터 요청
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 완료된 후에 수행할 작업
            }
        })

        binding.event.setOnClickListener {
            findNavController().navigate(R.id.action_foodTruckFragment_to_eventFragment)
        }

    }

}
