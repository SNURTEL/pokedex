package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class SpeciesModel(
    val name: String,
    val is_baby: Boolean,
    val is_mythical: Boolean,
    val is_legendary: Boolean,
    val evolution_chain: ApiResourceModel
)