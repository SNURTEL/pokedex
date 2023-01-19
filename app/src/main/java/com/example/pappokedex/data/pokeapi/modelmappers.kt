package com.example.pappokedex.data.pokeapi

import com.example.pappokedex.data.pokeapi.models.SingleAbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel
import com.example.pappokedex.domain.Ability
import com.example.pappokedex.domain.Pokemon

fun mapModelsToPokemon(
    pokemonModel: PokemonModel,
    speciesModel: SpeciesModel,
    abilityNames: List<String>,
    abilityModels: List<SingleAbilityModel>
) =
    with(pokemonModel) {
        with(speciesModel) {
            Pokemon(
                name = name,
                iconUrl = sprites.front_default ?: "",
                height = height,
                weight = weight,
                abilities = abilityModels.zip(abilityNames).map {
                    mapModelToAbility(it.first, it.second)
                },
                types = types.map { it.type.name },
                evolutionChainId = evolution_chain?.let {
                        it.url
                        .split('/')
                        .dropLast(1)
                        .last()
                        .toInt() // save an API call by extracting ID from URL
                },

                isBaby = is_baby,
                isLegendary = is_legendary,
                isMythical = is_mythical
            )
        }
    }


fun mapModelToAbility(abilityModel: SingleAbilityModel, abilityName: String) =
    Ability(
        name = abilityName,
        effect_description = abilityModel.effect_entries
            .find { vem -> vem.language.name == "en" }
            ?.effect ?: ""
    )