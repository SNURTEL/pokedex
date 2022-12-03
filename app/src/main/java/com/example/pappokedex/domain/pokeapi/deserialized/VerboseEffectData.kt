package com.example.pappokedex.domain.pokeapi.deserialized

import kotlinx.serialization.Serializable

@Serializable
data class VerboseEffectData(
    var effectDescription: String,
    var effectShortDesc: String
)