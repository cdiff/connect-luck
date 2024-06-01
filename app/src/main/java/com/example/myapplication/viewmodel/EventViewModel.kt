package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.EventApi
import com.example.data.dto.EventHeader
import com.example.data.dto.FoodTruckHeader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventApi: EventApi
) : ViewModel() {

    private val _eventList1 = MutableLiveData<List<EventHeader>>()
    val eventList2: LiveData<List<EventHeader>> = _eventList1

    private val _eventList = MutableLiveData<List<EventHeader>>()
    val eventList: LiveData<List<EventHeader>> get() = _eventList


    // 이벤트 데이터를 로드하는 메서드
    fun loadEvents() {
        viewModelScope.launch {
            val response = eventApi.getEvents()
            if (response.isSuccessful) {
                _eventList.postValue(response.body())
            } else {
                // 에러 처리
            }
        }
    }

    fun searchEvent(status: String) {
        viewModelScope.launch {
            try {
                val response = eventApi.searchEvent(status)

                if (response.isSuccessful) {
                    val eventList = response.body() ?: emptyList()
                    Log.d("FoodTruckViewModel", "evvvvvaaavenbr ${eventList}")

                    _eventList1.value = eventList
                    Log.d("FoodTruckViewModel", "evvvvvvenbr ${_eventList1}")

                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}
