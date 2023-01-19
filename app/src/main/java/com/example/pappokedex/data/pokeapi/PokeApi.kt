package com.example.pappokedex.data.pokeapi

import com.example.pappokedex.data.pokeapi.models.SingleAbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonResourceListModel
import retrofit2.Response
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

    @GET("pokemon-species/{speciesId}")
    suspend fun getSpecies(@Path("speciesId") speciesId: Int): Response<SpeciesModel>

    @GET("ability/{abilityName}")
    suspend fun getAbility(@Path("abilityName") name: String): Response<SingleAbilityModel>

    @GET("ability/{abilityId}")
    suspend fun getAbility(@Path("abilityId") abilityId: Int): Response<SingleAbilityModel>
}
