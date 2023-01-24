package com.example.pappokedex.domain

data class PokemonSnapshot(
    val name: String,
    val iconUrl: String,
    val types: List<String>,
)