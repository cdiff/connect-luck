package com.example.data.api


import com.example.data.dto.FoodTruckHeader
import com.example.data.dto.FoodTruckMenu
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodTruckApi {

    /**
     * 푸드트럭 검색 기능
     * @param name 푸드트럭 이름
     * @param foodType 음식 종류
     */
    @GET("/api/food-truck")
    suspend fun searchFoodTruck(
        @Query("name") name: String,
    ): Response<List<FoodTruckHeader>>

    @GET("/api/food-truck/{id}")
    suspend fun getFoodTruck(
        @Path("id") id: Int
    ): Response<List<FoodTruckMenu>>


    @GET("/api/food-truck/{foodTruckId}/menu")
    suspend fun getFoodTruckMenu(
        @Path("foodTruckId") id: Int,
    ): Response<List<FoodTruckMenu>>


    @Multipart
    @POST("/api/food-truck")
    suspend fun registerFoodTruck(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("foodType") foodType: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @Multipart
    @PATCH("/api/food-truck/{id}")
    suspend fun updateFoodTruck(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("foodType") foodType: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("/api/food-truck/my")
    suspend fun findMyTruck(
        @Header("Authorization") token: String,
    ): Response<List<FoodTruckHeader>>


    @Multipart
    @POST("/api/food-truck/{foodTruckId}/menu")
    suspend fun addFoodTruckMenu(
        @Header("Authorization") token: String,
        @Path("foodTruckId") foodTruckId: Int,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @POST("/api/application")
    fun updateFoodTruck(
        @Header("Authorization") token: String,
        @Body request: UpdateFoodTruckRequest

    ): Response<ResponseBody>

    data class UpdateFoodTruckRequest(
        val eventId: Int,
        val foodTruckId: Int,
        val comment: String
    )

}
