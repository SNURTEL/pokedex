package com.example.pappokedex.domain.pokeapi.deserialized

import kotlinx.serialization.Serializable

@Serializable
data class AbilityData(
    val is_hidden: Boolean,
    val slot: Int,
    val ability: NamedApiResourceData
    )