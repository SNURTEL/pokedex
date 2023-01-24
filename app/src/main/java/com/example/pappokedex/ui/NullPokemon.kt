package com.example.pappokedex.ui

import com.example.pappokedex.domain.Ability
import com.example.pappokedex.domain.Pokemon

fun getNullPokemon(): Pokemon {
    val run = Ability("Run Away", "Ensures success fleeing from wild battles.")
    val adapt = Ability("Adaptability", "Increases the same-type attack bonus from 1.5× to 2×.")
    val abilities: List<Ability> = listOf(run, adapt)
    val pokType: List<String> = listOf("Normal")
    return Pokemon(
        "Eevee",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png",
        3,
        65,
        abilities,
        pokType,
        69,
        true,
        false,
        false
    )
}