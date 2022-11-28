package com.example.pappokedex.domain

import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonSnapshots(): Flow<List<PokemonSnapshot>>

    suspend fun getPokemon(): Result<Pokemon>
}