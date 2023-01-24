package com.example.pappokedex.domain

import kotlinx.coroutines.flow.StateFlow

interface PokemonRepository {
    val snapshotsFlow: StateFlow<List<PokemonSnapshot>>
    val favouritesFlow: StateFlow<List<PokemonSnapshot>>

    suspend fun getAllSnapshots()
    suspend fun getFavoriteSnapshots()
    suspend fun getPokemon(name: String): Pokemon?
    suspend fun setFavoritePokemon(name: String)
    suspend fun removeFavoritePokemon(name: String)
}