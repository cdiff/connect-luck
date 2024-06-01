package com.example.myapplication.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDescriptionBinding
import com.example.myapplication.viewmodel.FoodTruckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodTruckViewModel: FoodTruckViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)

        foodTruckViewModel = ViewModelProvider(requireActivity()).get(FoodTruckViewModel::class.java)

        val foodTruck = arguments?.getSerializable("selectedFoodTruck") as? FoodTruckHeader

        foodTruck?.let { selectedFoodTruck ->
            Log.d("DescriptionFragment", "Selected FoodTruck: ${selectedFoodTruck.name}")
            binding.textView1.text = selectedFoodTruck.foodType
            binding.textView2.text = selectedFoodTruck.name
            binding.textView3.text = selectedFoodTruck.description
            binding.textView4.text = selectedFoodTruck.managerName

            binding.ratingBar.rating = selectedFoodTruck.avgRating.toFloat()



            Glide.with(requireContext())
                .load(selectedFoodTruck.imageUrl)
                .into(binding.imageView2)

            // FoodTruckViewModel을 사용하여 푸드트럭의 메뉴를 가져오기
            foodTruckViewModel.getFoodTruckMenu(selectedFoodTruck.id)
        } ?: run {
            Log.d("DescriptionFragment", "No selected food truck")
        }

        // FoodTruckViewModel에서 푸드트럭의 메뉴를 관찰하여 UI 업데이트
        foodTruckViewModel.menuList.observe(viewLifecycleOwner) { menuList ->
            val menuAdapter = FoodTruckMenuAdapter(requireContext(), menuList, nestedScrollView)
            binding.menuList.adapter = menuAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
