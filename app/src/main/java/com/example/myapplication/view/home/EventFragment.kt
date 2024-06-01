package com.example.myapplication.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEventBinding
import com.example.myapplication.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private lateinit var eventAdapter: EventAdapterList
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)

        // 행사 어댑터 초기화
        eventAdapter = EventAdapterList(requireContext(), emptyList(), nestedScrollView, R.id.eventFragment)
        binding.eventlist.adapter = eventAdapter

        // 스피너 초기화
        val spinner: Spinner = view.findViewById(R.id.eventSpinner)

        // 스피너에 데이터 추가
        val eventTypes = listOf(
            "BEFORE_APPLICATION",
            "OPEN_FOR_APPLICATION",
            "APPLICATION_FINISHED",
            "EVENT_START",
            "EVENT_END"
        )
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // 스피너 아이템 선택 이벤트 처리
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                eventViewModel.searchEvent(selectedItem)

                // 선택된 아이템에 맞게 이벤트 검색
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
            }
        }

        // FoodTruckViewModel에서 데이터 가져와서 ListView에 표시
        eventViewModel.eventList2.observe(viewLifecycleOwner) { foodTruckHeaders ->
            eventAdapter.updateData(foodTruckHeaders)
        }

        binding.foodtruck.setOnClickListener {
            findNavController().navigate(R.id.action_eventFragment_to_foodTruckFragment)
        }
    }
}