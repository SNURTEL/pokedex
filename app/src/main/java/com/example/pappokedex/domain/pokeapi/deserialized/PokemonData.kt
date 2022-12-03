package com.example.pappokedex.domain.pokeapi.deserialized

import kotlinx.serialization.Serializable

@Serializable
data class PokemonData (
    val name: String,
//    val iconUrl: String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilityData>,
    val types: List<PokemonTypeData>,
//    val evolutionChainId: Int,
//    val isBaby: Boolean,
//    val isLegendary: Boolean,
//    val isMythical: Boolean
        )