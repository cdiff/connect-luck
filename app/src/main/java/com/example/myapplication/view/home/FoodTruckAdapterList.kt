package com.example.myapplication.view.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R

class FoodTruckAdapterList(
    private val context: Context,
    private var foodTrucks: List<FoodTruckHeader>,
    private val nestedScrollView: NestedScrollView,
    private val fromFragmentId: Int
) : BaseAdapter() {

    override fun getCount(): Int {
        return foodTrucks.size
    }

    override fun getItem(position: Int): FoodTruckHeader {
        return foodTrucks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_food_truck, parent, false)
        } else {
            view = convertView
        }

        val foodTruck = getItem(position)

        val nameTextView = view.findViewById<TextView>(R.id.foodTruckName)
        val typeTextView = view.findViewById<TextView>(R.id.foodTruckType)
        val imageView = view.findViewById<ImageView>(R.id.foodTruckImage)

        nameTextView.text = foodTruck.name
        typeTextView.text = foodTruck.foodType

        Glide.with(context)
            .load(foodTruck.imageUrl)
            .into(imageView)

        view.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("selectedFoodTruck", foodTruck)
            }
            when (fromFragmentId) {
                R.id.homeFragment -> {
                    view.findNavController().navigate(R.id.action_homeFragment_to_descriptionFragment, bundle)
                }
                R.id.foodTruckFragment -> {
                    view.findNavController().navigate(R.id.action_foodTruckFragment_to_descriptionFragment, bundle)
                }
                R.id.mytruckFragment -> {
                    val options = arrayOf("푸드트럭", "메뉴")
                    AlertDialog.Builder(context)
                        .setTitle("무엇을 수정하시겠습니까?")
                        .setItems(options) { dialog, which ->
                            val selectedOption = options[which]
                            when (selectedOption) {
                                "푸드트럭" -> {
                                    view.findNavController().navigate(R.id.action_mytruckFragment_to_myTruckUpdateFragment, bundle)
                                }
                                "메뉴" -> {
                                    view.findNavController().navigate(R.id.action_mytruckFragment_to_foodTruckMenuFragment, bundle)
                                }
                            }
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }


        view.setOnTouchListener { _, _ ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        return view
    }

    fun updateData(newFoodTrucks: List<FoodTruckHeader>) {
        foodTrucks = newFoodTrucks
        notifyDataSetChanged()
    }
}
