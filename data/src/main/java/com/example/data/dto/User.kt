package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("role") var role: String? = null
) {
}