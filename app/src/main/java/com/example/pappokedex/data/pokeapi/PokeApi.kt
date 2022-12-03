package com.example.pappokedex.data.pokeapi

import com.example.pappokedex.data.pokeapi.models.AbilityModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel

interface PokeApi {
    @GET("pokemon/{pokeName}")
    suspend fun getPokemon(@Path("pokeName") name: String): Response<PokemonModel>

    @GET("pokemon/?limit=-1")
    suspend fun getAllPokeResources(): Response<PokeResourceList>

    @Serializable
    data class PokeResourceList(val count: Int,
                                val next: String?,
                                val previous: String?,
                                val results: List<PokeResource>)

    @Serializable
    data class PokeResource(val name: String, val url: String)

    @GET("pokemon-species/{speciesName}")
    suspend fun getSpecies(@Path("speciesName") name: String): Response<SpeciesModel>

    @GET("ability/{abilityName}")
    suspend fun getAbility(@Path("abilityName") name: String): Response<AbilityModel>
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
