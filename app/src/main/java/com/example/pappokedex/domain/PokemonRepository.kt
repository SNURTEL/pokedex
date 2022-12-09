package com.example.pappokedex.domain

interface PokemonRepository {
    suspend fun getPokemonSnapshots(): List<PokemonSnapshot>
    suspend fun getPokemon(name: String): Pokemon?
}