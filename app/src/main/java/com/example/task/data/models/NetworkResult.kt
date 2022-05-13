package com.example.task.data.models

sealed class NetworkResult<T : Any> {
    class ApiSuccess<T: Any>(val data: T?, val code: Int) : NetworkResult<T>()
    class ApiError<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class ApiException<T: Any>(val e: Throwable) : NetworkResult<T>()
}