package com.example.data.dto

import java.io.Serializable

data class EventHeader(
    val id: Int,
    val title: String,
    val content: String,
    val zipCode: String,
    val streetAddress: String,
    val detailAddress: String,
    val startAt: String,
    val endAt: String,
    val imageUrl: String,
    val managerName: String,
    val status: String
) : Serializable
