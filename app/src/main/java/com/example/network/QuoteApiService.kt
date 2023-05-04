package com.example.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.quotable.io"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(
    MoshiConverterFactory.create(moshi)
).baseUrl(BASE_URL).build()

interface QuoteApiService {
    @GET("quotes/random")
    suspend fun getQuote(
        @Query("maxLength") maxLength: Int, @Query("minLength") minLength: Int
    ): List<Map<String, Any>>
}

object QuoteApi {
    val retrofitService: QuoteApiService by lazy { retrofit.create(QuoteApiService::class.java) }
}