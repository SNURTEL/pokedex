package com.example.pappokedex.domain

interface PokemonRepository {
    suspend fun getAllSnapshots(): List<PokemonSnapshot>
    suspend fun getFavoriteSnapshots(): List<PokemonSnapshot>
    suspend fun getPokemon(name: String): Pokemon?
    suspend fun setFavoritePokemon(name: String): Unit
    suspend fun removeFavoritePokemon(name: String): Unit
}