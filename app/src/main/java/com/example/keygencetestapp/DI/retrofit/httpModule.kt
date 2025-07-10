package com.example.keygencetestapp.DI.retrofit

import com.example.keygencetestapp.api.SmapriApi
import com.example.keygencetestapp.constant.RetrofitConstant
import com.example.keygencetestapp.database.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    @Provides
    @Singleton
    fun provideBaseUrlHolder(): BaseUrlHolder = BaseUrlHolder()

    @Provides
    @Singleton
    fun provideBaseUrlInterceptor(baseUrlHolder: BaseUrlHolder): BaseUrlInterceptor =
        BaseUrlInterceptor(baseUrlHolder)

    @Provides
    @Singleton
    fun provideOkHttpClient(baseUrlInterceptor: BaseUrlInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(baseUrlInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RetrofitConstant.SMAPRI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSmaPriAPI(
        database: StockDatabase,
        retrofit: Retrofit
    ): SmapriApi {
        return retrofit.create(SmapriApi::class.java)
    }

}