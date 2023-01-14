package com.example.pappokedex.mock

import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.entities.*
import com.example.pappokedex.domain.Pokemon


fun getMockPokemonEntity(): PokemonEntity =
    PokemonEntity(
        name = "bulbasaur",
        iconUrl = "https://weiti.pl",
        height = 666,
        weight = 777,
        types = listOf("cool type"),
        evolutionChainId = 999,
        isBaby = true,
        isMythical = false,
        isLegendary = false,
    )


class MockPokemonDao : PokemonDao {
    override suspend fun getPokemon(name: String): PokemonEntity? {
        return getMockPokemonEntity()
    }

    override suspend fun getPokemonAbilities(name: String): List<AbilityEntity> {
        return listOf()
//        return super.getPokemonAbilities(name)
    }

    override suspend fun insertPokemonData(pokemonData: List<Pokemon>) {
//        super.insertPokemonData(pokemonData)
    }

    override fun insertPokemons(pokemons: List<PokemonEntity>) {
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
