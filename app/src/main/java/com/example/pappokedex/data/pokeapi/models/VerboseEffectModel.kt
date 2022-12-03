package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class VerboseEffectModel(
    val effect: String,
    val short_effect: String,
    val language: NamedApiResourceModel
)