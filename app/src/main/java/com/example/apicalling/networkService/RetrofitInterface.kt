package com.example.apicalling.networkService

import com.example.apicalling.model.ItemsModel
import retrofit2.http.*

interface RetrofitInterface {

    @GET(AllApi.users)
    suspend fun getFlowerList(@Query("page") page: String): ItemsModel

}













