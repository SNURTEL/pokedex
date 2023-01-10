package com.example.pappokedex.data.database

import com.example.pappokedex.data.database.entities.AbilityEntity
import com.example.pappokedex.data.database.entities.FavoritePokemon
import com.example.pappokedex.data.database.entities.PokemonEntity
import com.example.pappokedex.data.database.entities.PokemonSnapshotEntity
import com.example.pappokedex.domain.Ability
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonSnapshot

fun mapPokemonEntityToDomain(pokemonEntity: PokemonEntity, abilities: List<AbilityEntity>) =
    with(pokemonEntity) {
        Pokemon(
            name = name,
            iconUrl = iconUrl,
            height = height,
            weight = weight,
            abilities = abilities.map(::abilityEntityToDomain),
            types = types,
            evolutionChainId = evolutionChainId,
            isBaby = isBaby,
            isLegendary = isLegendary,
            isMythical = isMythical
        )
    }

fun mapPokemonDomainToEntity(pokemon: Pokemon) =
    with(pokemon) {
        PokemonEntity(
            name = name,
            iconUrl = iconUrl,
            height = height,
            weight = weight,
            types = types,
            evolutionChainId = evolutionChainId,
            isBaby = isBaby,
            isLegendary = isLegendary,
            isMythical = isMythical
        )
    }

fun abilityEntityToDomain(abilityEntity: AbilityEntity) =
    with(abilityEntity) {
        Ability(name, effectDescription)
    }

fun abilityDomainToEntity(ability: Ability) =
    with(ability) {
        AbilityEntity(name, effect_description)
    }

fun mapPokemonSnapshotEntityToDomain(snapshotEntity: PokemonSnapshotEntity) =
    with(snapshotEntity) {
        PokemonSnapshot(name, iconUrl, types)
    }

fun domainToFavoritePokemon(pokemon: Pokemon) = FavoritePokemon(pokemon.name)

fun domainToFavoritePokemon(pokemon: PokemonSnapshot) = FavoritePokemon(pokemon.name)
