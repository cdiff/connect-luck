package com.example.myapplication.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R

class FoodTruckAdapter(
    private val context: Context,
    private var foodTruckList: List<FoodTruckHeader>,
    private val fromFragmentId: Int,
    private val onItemClick: (FoodTruckHeader) -> Unit // 클릭 콜백 추가
) : RecyclerView.Adapter<FoodTruckAdapter.FoodTruckViewHolder>() {

    private val selectedPositions: MutableSet<Int> = mutableSetOf()

    inner class FoodTruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textImg)
        val textView1: TextView = itemView.findViewById(R.id.description)
        val textView2: TextView = itemView.findViewById(R.id.rating)
        val textView3: TextView = itemView.findViewById(R.id.foodtype)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedPositions.contains(position)) {
                        selectedPositions.remove(position)
                    } else {
                        selectedPositions.add(position)
                    }
                    notifyDataSetChanged() // 변경된 상태 반영
                    onItemClick(foodTruckList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTruckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_truck, parent, false)
        return FoodTruckViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodTruckViewHolder, position: Int) {
        val foodTruck = foodTruckList[position]
        Glide.with(context)
            .load(foodTruck.imageUrl)
            .into(holder.imageView)

        if (fromFragmentId == R.id.homeFragment) {
            holder.textView.text = foodTruck.name
            holder.textView1.text = foodTruck.description
            holder.textView2.text = foodTruck.avgRating.toString()
            holder.textView3.text = foodTruck.foodType
        } else if (fromFragmentId == R.id.eventApplicationFragment) {
            holder.textView.text = foodTruck.managerName
            holder.textView1.text = foodTruck.name
            holder.textView2.text = foodTruck.avgRating.toString()
            holder.textView3.text = foodTruck.foodType
        }

        // 클릭된 FoodTruck에 대해 배경색 변경
        val isSelected = selectedPositions.contains(position)
        holder.itemView.isSelected = isSelected
        holder.textView.isSelected = isSelected
        holder.textView1.isSelected = isSelected
        holder.textView2.isSelected = isSelected
        holder.textView3.isSelected = isSelected
    }

    override fun getItemCount(): Int {
        return foodTruckList.size
    }

    fun updateData(newFoodTruckList: List<FoodTruckHeader>) {
        foodTruckList = newFoodTruckList
        notifyDataSetChanged()
    }

    fun getSelectedFoodTruckId(): Int? {
        return selectedPositions.firstOrNull()?.let { foodTruckList[it].id }
    }
}
