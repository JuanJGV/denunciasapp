package com.guerrerovillalobos.denunciasapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://denunciaservice-guerrerovillalobos.onrender.com/" // Tu URL de Render[cite: 3, 23]

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(70, TimeUnit.SECONDS) // Previene timeout si Render está en reposo[cite: 3, 21]
        .readTimeout(70, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: DenunciaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DenunciaApi::class.java)
    }
}