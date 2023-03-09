package com.example.apicalling.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.repository.ItemRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ItemViewModel(private var itemRepository: ItemRepository) : ViewModel() {

    /**
     * Instead of using live data using flow
     */
    val wMessage: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    fun getFlowerList(option: String) = viewModelScope.launch {

        wMessage.value = ApiState.Loading

        itemRepository.getFlowerList(option)
            .catch { e -> wMessage.value = ApiState.Failure(e)

            }
            .collect { data ->
                wMessage.value = ApiState.Success(data)
            }
    }
   /* private fun getErrorMessageFromGenericResponse(httpException: HttpException): String? {
        var errorMessage: String? = null
        try {
            val body = httpException.response()?.errorBody()
            val adapter = Gson().getAdapter(GenericResponse::class.java)
            val errorParser = adapter.fromJson(body?.string())
            errorMessage = errorParser.errorMessage?.get(0)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return errorMessage
        }
    }*/

}