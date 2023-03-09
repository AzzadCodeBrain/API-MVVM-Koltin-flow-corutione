package com.example.apicalling.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ItemViewModel(private var itemRepository: ItemRepository): ViewModel() {

    /**
     * Instead of using live data using flow
     */
    val wMessage: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    fun getFlowerList(option: String) = viewModelScope.launch {
        wMessage.value = ApiState.Loading
        itemRepository.getFlowerList(option)
            .catch { e ->
                wMessage.value = ApiState.Failure(e)
            }.collect { data ->
                wMessage.value = ApiState.Success(data)
            }
    }



}