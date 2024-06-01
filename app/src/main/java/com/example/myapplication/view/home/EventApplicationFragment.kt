package com.example.myapplication.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.data.dto.EventHeader
import com.example.myapplication.R
import com.example.myapplication.common.MySharedPreferences
import com.example.myapplication.databinding.FragmentEventApplicationBinding
import com.example.myapplication.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventApplicationFragment : Fragment() {

    private var _binding: FragmentEventApplicationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var foodTruckAdapter: FoodTruckAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodTruckAdapter = FoodTruckAdapter(requireContext(), emptyList(), R.id.eventApplicationFragment) { foodTruck ->
            // 클릭 이벤트 처리
            val foodTruckId = foodTruck.id

        }
        binding.recyclerViewFoodTrucks.adapter = foodTruckAdapter

        val token = MySharedPreferences.getToken(requireContext())
        viewModel.fetchMyTruck(token)


        viewModel.myTruckList.observe(viewLifecycleOwner) { foodTruckHeaders ->
            foodTruckAdapter.updateData(foodTruckHeaders)
        }


        // Fetch food trucks

        // Retrieve the selectedEvent data from the bundle
        val selectedEvent = arguments?.getSerializable("selectedEvent") as? EventHeader
        selectedEvent?.let { event ->
            // Use the event data in your fragment as needed
            binding.managerName.text = event.managerName
            binding.title.text = event.title
            binding.startAt.text = event.startAt

            // Load event image
            Glide.with(requireContext())
                .load(event.imageUrl)
                .into(binding.eventimage)
        }

        binding.submitButton.setOnClickListener {
            val foodTruckId = foodTruckAdapter.getSelectedFoodTruckId()
            if (foodTruckId != null) {
                val token = MySharedPreferences.getToken(requireContext())
                val eventId = selectedEvent?.id ?: return@setOnClickListener
                val comment = binding.text.toString()

                Log.d("FoodTruckViewModel", "Error fetching food truck menu: $eventId, $foodTruckId")


                viewModel.updateFoodTruck(token, eventId, foodTruckId, comment)
            } else {
                // 사용자에게 푸드트럭을 선택하라는 메시지 표시
                Toast.makeText(requireContext(), "Please select a food truck", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
