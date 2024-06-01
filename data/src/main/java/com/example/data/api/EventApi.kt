package com.example.data.api

import com.example.data.dto.EventHeader
import com.example.data.dto.FoodTruckHeader
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {

    /**
     * 모든 이벤트를 로드합니다.
    */
    @GET("/api/event")
    suspend fun getEvents(): Response<List<EventHeader>>

    @GET("/api/event")
    suspend fun searchEvent(
        @Query("eventStatus") status: String,
    ): Response<List<EventHeader>>
}
