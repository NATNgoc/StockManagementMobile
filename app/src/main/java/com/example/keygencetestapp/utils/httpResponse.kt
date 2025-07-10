package com.example.keygencetestapp.utils

sealed class ResponseState<T>(data: T?, message: String?) {
    data class Success<T>(val data: T, val message: String) : ResponseState<T>(data, message)
    data class Error<T>(val data: T?, val message: String) : ResponseState<T>(data, message)
    data class Loading<T>(val message: String): ResponseState<T>(null, message)
    data class Idle<T>(val data: T? = null, val message: String? = null): ResponseState<T>(null, null)
}