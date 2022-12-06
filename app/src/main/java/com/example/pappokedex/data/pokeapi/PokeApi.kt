package com.example.pappokedex.data.pokeapi

import com.example.pappokedex.data.pokeapi.models.AbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonResourceListModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
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
    suspend fun getAllPokeResources(): Response<PokemonResourceListModel>

    @GET("pokemon-species/{speciesName}")
    suspend fun getSpecies(@Path("speciesName") name: String): Response<SpeciesModel>

    @GET("ability/{abilityName}")
    suspend fun getAbility(@Path("abilityName") name: String): Response<AbilityModel>
}
