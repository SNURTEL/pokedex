package com.example.pappokedex.data

import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.mapPokemonEntityToDomain
import com.example.pappokedex.data.database.mapPokemonSnapshotEntityToDomain
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
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
//    private val pokemonCache = RepositoryCache<String, Pokemon>();
//    private val pokemonSnapshotCache = RepositoryCache<String, PokemonSnapshot>();

    override suspend fun getPokemonSnapshots(): List<PokemonSnapshot> =
        withContext(Dispatchers.IO) {
            val response = pokemonRemoteApi.getAllPokeResources()

            val pokemonResources =
                response.body()?.results?.slice(0..29) ?: return@withContext listOf()

            val pokemonSnapshotsFromDb = pokemonDatabaseDao.getPokemonSnapshots().map {
                mapPokemonSnapshotEntityToDomain(it)
            }
            // works properly only after disabling the 30 call limit!
            if (pokemonResources.count() != pokemonSnapshotsFromDb.count()) {
                val missingPokemonNames = pokemonResources
                    .map { it.name }
                    .minus(pokemonSnapshotsFromDb.map { it.name }.toSet())

                pokemonDatabaseDao.insertPokemonData(missingPokemonNames.map { pokemonName ->
                    async { getPokemonFromRemoteApi(pokemonName) }
                }.awaitAll()
                    .also {
                        if (it.contains(null)) {
                            return@withContext listOf()
                        }
                    }.filterNotNull()
                )
            }

            pokemonDatabaseDao.getPokemonSnapshots().map(::mapPokemonSnapshotEntityToDomain)
        }


    override suspend fun getPokemon(name: String): Pokemon? =
        withContext(Dispatchers.IO) {
            getPokemonFromDB(name) ?: getPokemonFromRemoteApi(name)?.also {
                pokemonDatabaseDao.insertPokemonData(listOf(it))
            }
        }
//
//    private fun getPokemonFromCache(name: String): Pokemon? =
//        pokemonCache.getOrNull(name)

    private suspend fun getPokemonFromDB(name: String): Pokemon? =
        coroutineScope {
            val entity = let { pokemonDatabaseDao.getPokemon(name) } ?: return@coroutineScope null
            entity.let {
                val pokeAbilities = pokemonDatabaseDao.getPokemonAbilities(name)
                mapPokemonEntityToDomain(it, pokeAbilities)
            }
        }

    private suspend fun getPokemonFromRemoteApi(pokemonName: String): Pokemon? =
        coroutineScope {
            val model =
                getResponseBodyOrNull { pokemonRemoteApi.getPokemon(pokemonName) }
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
                    .also {
                        if (it.contains(null)) {
                            return@coroutineScope null
                        }
                    }
                    .filterNotNull()

            val speciesModel = getResponseBodyOrNull { pokemonRemoteApi.getSpecies(pokemonName) }
                ?: return@coroutineScope null

            mapModelsToPokemon(model, speciesModel, abilityModels)
        }
}

private suspend fun <T> getResponseBodyOrNull(query: suspend () -> Response<T>) = query().body()
