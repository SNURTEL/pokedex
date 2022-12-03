package com.example.pappokedex.domain.pokeapi.deserialized

import kotlinx.serialization.Serializable

@Serializable
data class NamedApiResourceData (
    val name: String,
    val url: String
        )