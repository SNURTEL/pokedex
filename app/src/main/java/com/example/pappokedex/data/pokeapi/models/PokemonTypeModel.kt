package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeModel (
    val slot: Int,
    val type: NamedApiResourceModel
        )