package com.example.data.dto

data class FoodTruckHeader(
    var id: Int,
    var name: String,
    var description: String,
    var imageUrl: String,
    var managerName: String,
    var foodType: String,
    var reviewCount: Int,
    var avgScore: Double
) {}
