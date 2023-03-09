package com.example.apicalling.repository


import com.example.apicalling.model.ItemsModel
import com.example.apicalling.networkService.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ItemRepository() {

    fun getFlowerList(option: String) : Flow<ItemsModel> = flow {
        val p = RetrofitClient.retrofit.getFlowerList(option)
        emit(p)
    }.flowOn(Dispatchers.IO)



}