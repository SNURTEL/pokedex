package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class SpriteUrlsModel (
    val front_default: String?,
    val front_female: String?,
    val back_default: String?,
    val back_female: String?,
        )