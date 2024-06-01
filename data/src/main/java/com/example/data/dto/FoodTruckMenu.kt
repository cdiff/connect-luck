package com.example.data.dto

import java.io.Serializable

data class FoodTruckMenu(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val createdAt: String,
    val updatedAt: String
) : Serializable
