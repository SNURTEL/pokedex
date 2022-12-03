package com.example.pappokedex.data.pokeapi

import com.example.pappokedex.data.pokeapi.models.AbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel
import com.example.pappokedex.domain.Ability
import com.example.pappokedex.domain.Pokemon

fun mapModelsToPokemon(
    pokemonModel: PokemonModel,
    speciesModel: SpeciesModel,
    abilityModels: List<AbilityModel>
) =
    Pokemon(
        name = pokemonModel.name,
        iconUrl = pokemonModel.sprites.front_default,
        height = pokemonModel.height,
        weight = pokemonModel.weight,
        abilities = abilityModels.map {
            mapModelToAbility(it)
        },
        types = pokemonModel.types.map { it.type.name },
        evolutionChainId = speciesModel.evolution_chain.url
            .split('/')
            .dropLast(1)
            .last()
            .toInt(),  // save an API call by extracting ID from URL
        isBaby = speciesModel.is_baby,
        isLegendary = speciesModel.is_legendary,
        isMythical = speciesModel.is_mythical
    )

fun mapModelToAbility(abilityModel: AbilityModel) =
    Ability(
        name = abilityModel.name,
        effect_description = abilityModel.effect_entries
                            .find { vem -> vem.language.name == "en" }
                            ?.effect ?: ""
    )