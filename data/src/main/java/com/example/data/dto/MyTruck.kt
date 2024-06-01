package com.example.data.dto

import java.io.Serializable

data class MyTruck(
    var id: Int,
    var name: String,
    var description: String,
    var imageUrl: String,
    var managerName: String,
    var foodType: String

) : Serializable
