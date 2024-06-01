package com.example.data.dto

import java.io.Serializable

data class FoodTruckHeader(
    var id: Int,
    var name: String,
    var description: String,
    var imageUrl: String,
    var managerName: String,
    var foodType: String,
    var reviewCount: Int,
    var avgRating: Double
) : Serializable
