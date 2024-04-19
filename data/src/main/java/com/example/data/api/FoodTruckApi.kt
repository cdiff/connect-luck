package com.example.data.api


import com.example.data.dto.FoodTruckHeader
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodTruckApi {

    /**
     * 푸드트럭 검색 기능
     * @param name 푸드트럭 이름
     * @param foodType 음식 종류
     */
    @GET("/api/food-truck")
    fun searchFoodTruck(
        @Query("name") name: String,
        @Query("foodType") foodType: String
    ): Call<List<FoodTruckHeader>>
}
