package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.FoodTruckApi
import com.example.data.dto.FoodTruckHeader
import com.example.data.dto.FoodTruckMenu
import com.example.data.dto.FoodTruckRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class FoodTruckViewModel @Inject constructor(
    private val foodTruckApi: FoodTruckApi,
) : ViewModel() {

    private val _foodTruckList = MutableLiveData<List<FoodTruckHeader>>()
    val foodTruckList: LiveData<List<FoodTruckHeader>> = _foodTruckList

    private val _menuList = MutableLiveData<List<FoodTruckMenu>>()
    val menuList: LiveData<List<FoodTruckMenu>> = _menuList

    private val _foodTruckDetails = MutableLiveData<List<String>>()
    val foodTruckDetails: LiveData<List<String>> = _foodTruckDetails

    fun searchFoodTruck(name: String) {
        viewModelScope.launch {
            try {
                val response = foodTruckApi.searchFoodTruck(name)
                if (response.isSuccessful) {
                    val foodTruckList = response.body() ?: emptyList()
                    _foodTruckList.value = foodTruckList
                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

    fun getFoodTruckMenu(id: Int) {
        viewModelScope.launch {
            try {
                val response = foodTruckApi.getFoodTruckMenu(id)
                if (response.isSuccessful) {
                    val menuList = response.body() ?: emptyList()
                    _menuList.value = menuList
                    Log.d("FoodTruckViewModel", "Received food truck menu: ${_menuList.value}")
                } else {
                    Log.d("FoodTruckViewModel", "Failed to fetch food truck menu")
                }
            } catch (e: Exception) {
                Log.d("FoodTruckViewModel", "Error fetching food truck menu: $e")
            }
        }
    }

    fun getFoodTruckDetails(id: Int) {
        viewModelScope.launch {
            try {
                val response = foodTruckApi.getFoodTruck(id)
                if (response.isSuccessful) {
                    val foodTruckDetails = response.body() ?: emptyList()
                    _foodTruckDetails.value = foodTruckDetails.map { it.imageUrl }
                    // Log successful response
                    Log.d("FoodTruckViewModel", "Received food truck details: $foodTruckDetails")
                } else {
                    // Log unsuccessful response
                    Log.d("FoodTruckViewModel", "Failed to fetch food truck details")
                }
            } catch (e: Exception) {
                // Log error
                Log.e("FoodTruckViewModel", "Error fetching food truck details", e)
            }
        }
    }

}
