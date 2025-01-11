package com.example.umc_7th_hackathon

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    // base url
    private const val BASE_URL = "http://3.38.222.221:8080"

    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
}