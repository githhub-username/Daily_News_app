package com.devatrii.dailynews.repository

sealed class APIResponses<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T>() : APIResponses<T>()
    class Success<T>(private val mData: T?) : APIResponses<T>(data = mData)
    class Error<T>(private val error: String?) : APIResponses<T>(errorMessage = error)
}