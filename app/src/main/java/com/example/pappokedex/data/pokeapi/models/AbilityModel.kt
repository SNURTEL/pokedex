package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class AbilityModel(
    val is_hidden: Boolean,
    val slot: Int,
    val ability: NamedApiResourceModel
    )