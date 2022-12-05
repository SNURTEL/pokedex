package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResourceModel(val name: String, val url: String)