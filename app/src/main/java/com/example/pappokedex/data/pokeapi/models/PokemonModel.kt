package com.example.pappokedex.data.pokeapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonModel (
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<ResourceAbilityModel>,
    val types: List<PokemonTypeModel>,
    val sprites: SpriteUrlsModel,
    val species: NamedApiResourceModel,
        )