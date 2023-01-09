package com.example.pappokedex.data.pokeapi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class PokeApiHelper {
    private val baseUrl = "https://pokeapi.co/api/v2/"
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @ExperimentalSerializationApi
    fun getApi(): PokeApi {
        return retrofit.create(PokeApi::class.java)
    }
}
