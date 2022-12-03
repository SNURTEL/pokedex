package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class VerboseEffectModel(
    val effectDescription: String,
    val effectShortDesc: String
)