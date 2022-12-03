package com.example.pappokedex.domain.pokeapi.deserialized

import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeData (
    val slot: Int,
    val type: NamedApiResourceData
        )