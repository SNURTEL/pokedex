package com.example.pappokedex.mock

import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.entities.*
import com.example.pappokedex.data.database.mapPokemonDomainToEntity
import com.example.pappokedex.domain.Pokemon


fun getMockPokemonEntity(): PokemonEntity =
    PokemonEntity(
        name = "bulbasaur",
        iconUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/001.png",
        height = 123,
        weight = 777,
        types = listOf("cool type"),
        evolutionChainId = 999,
        isBaby = true,
        isMythical = false,
        isLegendary = false,
    )


class MockPokemonDao : PokemonDao {
    val inner_pokemons = mutableListOf<PokemonEntity>()

    override suspend fun getPokemon(name: String): PokemonEntity? {
        return getMockPokemonEntity()
    }

    override suspend fun getPokemonAbilities(name: String): List<AbilityEntity> {
        return listOf()
//        return super.getPokemonAbilities(name)
    }

    override suspend fun insertPokemonData(pokemonData: List<Pokemon>) {
        val entities = pokemonData.map { mapPokemonDomainToEntity(it) }
        insertPokemons(entities)
    }

    override fun insertPokemons(pokemons: List<PokemonEntity>) {
        inner_pokemons.addAll(pokemons.minus(inner_pokemons))
//        TODO("Not yet implemented")
    }

    override fun insertPokemonSnapshots(pokemonSnapshots: List<PokemonSnapshotEntity>) {
//        TODO("Not yet implemented")
    }

    override fun insertAbilities(abilities: List<AbilityEntity>) {
//        TODO("Not yet implemented")
    }

    override fun insertAbilityRelation(abilityRelation: PokemonToAbilityEntity) {
//        TODO("Not yet implemented")
    }

    override suspend fun getAbility(name: String): AbilityEntity? {
        return null
//        TODO("Not yet implemented")
    }

    override suspend fun getPokemonAbilitiesNames(pokemonName: String): List<String> {
        return listOf()
//        TODO("Not yet implemented")
    }

    override fun insertFavoritePokemon(favPokemon: FavoritePokemon) {
//        TODO("Not yet implemented")
    }

    override suspend fun getFavoritePokemonSnapshots(): List<PokemonSnapshotEntity> {
        return listOf()
//        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouritePokemons(name: String) {
//        TODO("Not yet implemented")
    }

    override suspend fun getPokemonSnapshots(): List<PokemonSnapshotEntity> {
        return listOf()
//        TODO("Not yet implemented")
    }
}
