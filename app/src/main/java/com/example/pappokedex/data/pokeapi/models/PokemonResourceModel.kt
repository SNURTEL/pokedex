package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResourceList(val count: Int,
                               val next: String?,
                               val previous: String?,
                               val results: List<PokemonResource>)

@Serializable
data class PokemonResource(val name: String, val url: String)