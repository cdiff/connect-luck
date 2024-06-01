package com.example.myapplication.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentMyfruckBinding
import com.example.myapplication.view.home.FoodTruckAdapterList
import com.example.myapplication.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MytruckFragment : Fragment() {
    private lateinit var binding: FragmentMyfruckBinding
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var foodTruckAdapter: FoodTruckAdapterList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyfruckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodTruckAdapter = FoodTruckAdapterList(requireContext(), listOf(), binding.scrollView, R.id.mytruckFragment)

        binding.foodTruckListView.adapter = foodTruckAdapter

        val token = MySharedPreferences.getToken(requireContext())

        // 데이터 가져오기
        viewModel.fetchMyTruck(token)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)



        foodTruckAdapter = FoodTruckAdapterList(requireContext(), emptyList(), nestedScrollView, R.id.mytruckFragment)
        binding.foodTruckListView.adapter = foodTruckAdapter



        viewModel.myTruckList.observe(viewLifecycleOwner) {  foodTruckHeaders->
            foodTruckAdapter.updateData(foodTruckHeaders)
        }

    }
}
