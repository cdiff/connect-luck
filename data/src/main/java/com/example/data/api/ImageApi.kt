package com.example.data.api

import com.example.data.dto.ImageUrlResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {

    @POST("/api/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageUrlResponse>

}