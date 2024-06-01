package com.example.myapplication.view.mypage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFoodtruckMenuBinding
import com.example.myapplication.view.home.FoodTruckMenuAdapter
import com.example.myapplication.viewmodel.FoodTruckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodTruckMenuFragment : Fragment() {
    private lateinit var binding: FragmentFoodtruckMenuBinding
    private lateinit var menuAdapter: FoodTruckMenuAdapter
    private lateinit var selectedFoodTruck: FoodTruckHeader
    private val viewModel: FoodTruckViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodtruckMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedFoodTruck = arguments?.getSerializable("selectedFoodTruck") as FoodTruckHeader

        // FoodTruckMenuAdapter를 생성하고 ListView에 설정합니다.
        menuAdapter = FoodTruckMenuAdapter(requireContext(), emptyList(), binding.scrollView)
        binding.foodTruckListView.adapter = menuAdapter

        // 푸드트럭 메뉴를 가져오는 함수를 호출합니다.
        viewModel.getFoodTruckMenu(selectedFoodTruck.id)

        // ViewModel에서 LiveData를 관찰하여 데이터가 변경될 때 어댑터를 업데이트합니다.
        viewModel.menuList.observe(viewLifecycleOwner) { menuList ->
            // 데이터를 어댑터에 설정합니다.
            menuAdapter.menuList = menuList
            menuAdapter.notifyDataSetChanged()
        }

        binding.newMenu.setOnClickListener{
            val bundle = Bundle().apply {
                putSerializable("id", selectedFoodTruck.id)
            }

            findNavController().navigate(R.id.action_foodTruckMenuFragment_to_foodTruckMenuCreateFragment, bundle)

        }
    }
}
