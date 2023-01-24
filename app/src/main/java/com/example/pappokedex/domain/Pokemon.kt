package com.example.pappokedex.domain

data class Pokemon(
    val name: String,
    val iconUrl: String,
    val height: Int,
    val weight: Int,
    val abilities: List<Ability>,
    val types: List<String>,
    val evolutionChainId: Int?,
    val isBaby: Boolean,
    val isLegendary: Boolean,
    val isMythical: Boolean
) {
    fun toSnapshot() =
        PokemonSnapshot(
            name = name,
            iconUrl = iconUrl,
            types = types
        )
}
