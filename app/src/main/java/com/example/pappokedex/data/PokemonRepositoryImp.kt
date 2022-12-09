package com.example.pappokedex.data

import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.mapPokemonEntityToDomain
import com.example.pappokedex.data.database.mapPokemonSnapshotEntityToDomain
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
import com.example.pappokedex.data.pokeapi.models.PokemonResourceModel
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.domain.PokemonSnapshot
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class PokemonRepositoryImp @Inject constructor(
    private val pokemonRemoteApi: PokeApi,
    private val pokemonDatabaseDao: PokemonDao
) : PokemonRepository {
    override suspend fun getPokemonSnapshots(): List<PokemonSnapshot> =
        withContext(Dispatchers.IO) {
            val response = pokemonRemoteApi.getAllPokeResources()

            val pokemonResources =
                response.body()?.results?.slice(0..30) ?: return@withContext listOf()

            val pokemons = pokemonResources.map { pokemonRes ->
                async { pokemonDomainFromResourceModel(pokemonRes) }
            }.awaitAll().filterNotNull()
            pokemonDatabaseDao.insertPokemonData(pokemons)

            pokemonDatabaseDao.getPokemonSnapshots().map(::mapPokemonSnapshotEntityToDomain)
        }

    private suspend fun pokemonDomainFromResourceModel(pokemon: PokemonResourceModel): Pokemon? =
        coroutineScope {
            val model =
                getResponseBodyOrNull { pokemonRemoteApi.getPokemon(pokemon.name) }
                    ?: return@coroutineScope null

            val abilityModels =
                model.abilities
                    .map { abilityModel ->
                        async {
                            getResponseBodyOrNull {
                                pokemonRemoteApi.getAbility(abilityModel.ability.name)
                            }
                        }
                    }
                    .awaitAll()
                    .filterNotNull()


            val speciesModel = getResponseBodyOrNull { pokemonRemoteApi.getSpecies(pokemon.name) }
                ?: return@coroutineScope null

            mapModelsToPokemon(model, speciesModel, abilityModels)
        }

    private suspend fun <T> getResponseBodyOrNull(query: suspend () -> Response<T>) = query().body()

    override suspend fun getPokemon(name: String): Pokemon? =
        withContext(Dispatchers.IO) {
            val entity = pokemonDatabaseDao.getPokemon(name) ?: return@withContext null
            val abilities = pokemonDatabaseDao.getPokemonAbilities(name)
            mapPokemonEntityToDomain(entity, abilities)
        }
}