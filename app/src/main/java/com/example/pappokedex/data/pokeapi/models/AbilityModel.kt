package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class AbilityModel(
    val name: String,
    val effect_entries: List<VerboseEffectModel>
    )