package com.example.myapplication.view.home

import EventAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.viewmodel.EventViewModel
import com.example.myapplication.viewmodel.FoodTruckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val foodTruckViewModel: FoodTruckViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var foodTruckAdapter: FoodTruckAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)


        // 푸드트럭 어댑터 초기화
        foodTruckAdapter = FoodTruckAdapter(requireContext(), emptyList(), R.id.homeFragment) { foodTruck ->
            // 클릭 이벤트 처리
        }
        binding.recyclerViewFoodTrucks.adapter = foodTruckAdapter

        eventAdapter = EventAdapter(requireContext(), emptyList())
        binding.eventListView.adapter = eventAdapter
        // FoodTruckViewModel에서 초기 데이터 가져와서 ListView에 표시
        foodTruckViewModel.searchFoodTruck("") // 초기 데이터 로드

        // EventViewModel에서 초기 데이터 가져와서 ListView에 표시
        eventViewModel.loadEvents() // 초기 데이터 로드

        // FoodTruckViewModel에서 데이터 가져와서 ListView에 표시
        foodTruckViewModel.foodTruckList.observe(viewLifecycleOwner) { foodTruckHeaders ->
            foodTruckAdapter.updateData(foodTruckHeaders)
        }

        // EventViewModel에서 데이터 가져와서 ListView에 표시
        eventViewModel.eventList.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateData(events)
        }

        // 검색 버튼 클릭 리스너 설정
        binding.imageView.setOnClickListener {
            val query = binding.searchButton.text.toString()
            if (query.isNotEmpty()) {
                foodTruckViewModel.searchFoodTruck(query)
            }
        }

        binding.searchButton.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchButton.text.toString()
                if (query.isNotEmpty()) {
                    foodTruckViewModel.searchFoodTruck(query)
                }
                return@OnEditorActionListener true
            }
            false
        })


        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myPageActivity)
        }

        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_eventFragment)
        }

        binding.button1.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_foodTruckFragment)
        }

    }

}
