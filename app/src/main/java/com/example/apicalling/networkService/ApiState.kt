package com.example.apicalling.networkService

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val e: Throwable) : ApiState()
    class Success(val data: Any) : ApiState()
    object Empty : ApiState()
}

sealed class HandleResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : HandleResource<T>()
    class Success<T>(data: T?) : HandleResource<T>(data)
    class Error<T>(message: String?, data: T? = null) : HandleResource<T>(data, message)
    class Failed<T>(message: String?, data: T? = null) : HandleResource<T>(data, message)
}