package com.example.myapplication.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.FoodTruckApi
import com.example.data.api.UserApi
import com.example.data.dto.FoodTruckHeader
import com.example.data.dto.MyTruck
import com.example.data.dto.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userApi: UserApi,
    private val foodTruckApi : FoodTruckApi
) : ViewModel() {

    private val _roleAdded = MutableLiveData<Boolean>()
    val roleAdded: LiveData<Boolean> get() = _roleAdded

    private val _foodTruckRegistered = MutableLiveData<Boolean>()
    val foodTruckRegistered: LiveData<Boolean> get() = _foodTruckRegistered

    private val _myTruckList = MutableLiveData<List<FoodTruckHeader>>()
    val myTruckList: LiveData<List<FoodTruckHeader>> = _myTruckList

    fun getUserInfo(token: String, callback: (User?) -> Unit) {
        val call = userApi.getMe("Bearer $token")
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }

    fun addFoodTruckManagerRole(token: String, role: String) {
        viewModelScope.launch {
            val call = userApi.addRole("Bearer $token", role)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    _roleAdded.value = response.isSuccessful
                    Log.d("FoodTruckViewModel", "Role added: ${_roleAdded.value}")
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    _roleAdded.value = false
                    Log.d("FoodTruckViewModel", "Failed to add role: ${_roleAdded.value}")
                }
            })
        }
    }

    suspend fun registerFoodTruck(
        token: String,
        name: String,
        description: String,
        imageFile: File,
        foodType: String
    ): Response<ResponseBody> {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val foodTypeRequestBody = foodType.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

        val bearerToken = "Bearer $token"

        return foodTruckApi.registerFoodTruck(bearerToken, nameRequestBody, foodTypeRequestBody, descriptionRequestBody, imagePart)
    }

    fun fetchMyTruck(token: String) {
        viewModelScope.launch {
            try {
                val response = foodTruckApi.findMyTruck("Bearer $token")
                if (response.isSuccessful) {
                    _myTruckList.value = response.body()
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun updateFoodTruck(token: String, id: Int, name: String, foodType: String, description: String, imageFile: File
    ): Response<ResponseBody> {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val foodTypeRequestBody = foodType.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

        val bearerToken = "Bearer $token"

        return foodTruckApi.updateFoodTruck(bearerToken, id, nameRequestBody, foodTypeRequestBody, descriptionRequestBody, imagePart)
    }

    suspend fun addFoodTruckMenu(token: String, foodTruckId: Int, name: String, price: Int, description: String, imageFile: File): Response<ResponseBody> {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequestBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        val bearerToken = "Bearer $token"
        return foodTruckApi.addFoodTruckMenu(bearerToken, foodTruckId, nameRequestBody, priceRequestBody, descriptionRequestBody, imagePart)
    }

    fun updateFoodTruck(token: String, eventId: Int, foodTruckId: Int, comment: String) {
        viewModelScope.launch {
            try {
                val response = foodTruckApi.updateFoodTruck(token,
                    FoodTruckApi.UpdateFoodTruckRequest(eventId, foodTruckId, comment)
                )
                if (response.isSuccessful) {
                    Log.e(TAG, "tjdrhd: ${response.code()}")

                    // 서버 요청이 성공한 경우
                    // 응답을 처리하거나 필요에 따라 다른 작업을 수행할 수 있습니다.
                } else {
                    Log.e(TAG, "서버 오류: ${response.code()}")

                    // 서버 요청이 실패한 경우
                    // 에러 처리 또는 사용자에게 알림을 표시하는 등의 작업을 수행할 수 있습니다.
                }
            } catch (e: Exception) { Log.e(TAG, "서버 오류:sdadsa")
                // 예외가 발생한 경우
                // 네트워크 연결 문제 또는 서버 오류 등을 처리할 수 있습니다.
            }
        }
    }

}
