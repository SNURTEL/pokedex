package com.example.pappokedex.domain.pokeapi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

import com.example.pappokedex.domain.pokeapi.deserialized.PokemonData

interface PokeApi {
    @GET("pokemon/{pokeName}")
    suspend fun getPokemon(@Path("pokeName") name: String): Response<PokemonData>

    @GET("pokemon/?limit=-1")
    suspend fun getAllPokeResources(): Response<PokeResourceList>

    @Serializable
    data class PokeResourceList(val count: Int,
                                val next: String?,
                                val previous: String?,
                                val results: List<PokeResource>)

    @Serializable
    data class PokeResource(val name: String, val url: String)
}

object PokeApiHelper {
    private const val baseUrl = "https://pokeapi.co/api/v2/"
    private val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true }

    @ExperimentalSerializationApi
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
