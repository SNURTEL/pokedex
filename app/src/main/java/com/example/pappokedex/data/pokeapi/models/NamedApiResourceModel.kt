package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class NamedApiResourceModel (
    val name: String,
    val url: String
        )