package com.example.keygencetestapp.DI.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor(private val baseUrlHolder: BaseUrlHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newBaseUrl = baseUrlHolder.baseUrl

        val originalHttpUrl = originalRequest.url
        val pathAndQuery =
            originalHttpUrl.encodedPath + if (originalHttpUrl.query != null) "?${originalHttpUrl.query}" else ""

        val newUrl = "$newBaseUrl$pathAndQuery"

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}