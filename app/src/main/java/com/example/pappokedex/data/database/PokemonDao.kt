package com.example.pappokedex.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pappokedex.data.database.entities.*
import com.example.pappokedex.domain.Pokemon

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_snapshots")
    suspend fun getPokemonSnapshots(): List<PokemonSnapshotEntity>

    @Query("SELECT * FROM pokemons WHERE name = :name LIMIT 1")
    suspend fun getPokemon(name: String): PokemonEntity?

    suspend fun getPokemonAbilities(name: String): List<AbilityEntity> =
        getPokemonAbilitiesNames(name).mapNotNull { getAbility(it) }

    suspend fun insertPokemonData(pokemonData: List<Pokemon>) {
        val pokemonEntities = pokemonData.map(::mapPokemonDomainToEntity)
        insertPokemons(pokemonEntities)

        val snapshotEntities = pokemonEntities.map(PokemonEntity::toSnapshot)
        insertPokemonSnapshots(snapshotEntities)

        val pokemonNameToAbilities = pokemonData.associate { pokemon ->
            pokemon.name to pokemon.abilities.map(::abilityDomainToEntity)
        }


        pokemonNameToAbilities.forEach { (pokemonName, abilities) ->
            insertAbilitiesRelations(pokemonName = pokemonName, abilities = abilities)
        }

        insertAbilities(pokemonNameToAbilities.values.flatten())
    }

    private suspend fun insertAbilitiesRelations(
        pokemonName: String,
        abilities: List<AbilityEntity>
    ) {
        if (getPokemonAbilitiesNames(pokemonName).isNotEmpty()) return

        abilities.forEach { ability ->
            val newRelation =
                PokemonToAbilityEntity(pokemonName = pokemonName, abilityName = ability.name)
            insertAbilityRelation(newRelation)
        }
    }


    // MANAGE NAMES!!!!!,
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPokemons(pokemons: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPokemonSnapshots(pokemonSnapshots: List<PokemonSnapshotEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAbilities(abilities: List<AbilityEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAbilityRelation(abilityRelation: PokemonToAbilityEntity)

    @Query("SELECT * FROM abilities WHERE name = :name LIMIT 1")
    suspend fun getAbility(name: String): AbilityEntity?

    @Query("SELECT abilityName FROM pokemon_to_ability WHERE pokemonName = :pokemonName")
    suspend fun getPokemonAbilitiesNames(pokemonName: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePokemon(favPokemon: FavoritePokemon)

    @Query("SELECT * FROM favorite_pokemons fp join pokemons p on fp.name = p.name")
    suspend fun getFavoritePokemonSnapshots(): List<PokemonSnapshotEntity>

    @Query("DELETE FROM favorite_pokemons WHERE name = :name")
    suspend fun deleteFavouritePokemons(name: String)
}