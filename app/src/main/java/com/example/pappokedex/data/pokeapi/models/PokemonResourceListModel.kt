package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable


@Serializable
data class PokemonResourceListModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResourceModel>
)