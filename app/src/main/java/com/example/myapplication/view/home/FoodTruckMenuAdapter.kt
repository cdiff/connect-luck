package com.example.myapplication.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.example.data.dto.FoodTruckMenu
import com.example.myapplication.R

class FoodTruckMenuAdapter(
    private val context: Context,
    var menuList: List<FoodTruckMenu>,
    private val nestedScrollView: NestedScrollView, // 스크롤뷰 추가

) : BaseAdapter() {

    override fun getCount(): Int {
        return menuList.size
    }

    override fun getItem(position: Int): FoodTruckMenu {
        return menuList[position]
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
        typeTextView.text = foodTruck.price.toString()


        Glide.with(context)
            .load(foodTruck.imageUrl)
            .into(imageView)

        view.setOnTouchListener { v, event ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        return view
    }

}
