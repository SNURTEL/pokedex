package com.example.pappokedex.data

import android.util.Log
import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.domainToFavoritePokemon
import com.example.pappokedex.data.database.entities.FavoritePokemon
import com.example.pappokedex.data.database.mapPokemonEntityToDomain
import com.example.pappokedex.data.database.mapPokemonSnapshotEntityToDomain
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.domain.PokemonSnapshot
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PokemonRepositoryImp @Inject constructor(
    private val pokemonRemoteApi: PokeApi,
    private val pokemonDatabaseDao: PokemonDao
) : PokemonRepository {

    override suspend fun getAllSnapshots(): List<PokemonSnapshot> =
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

    override suspend fun getFavoriteSnapshots(): List<PokemonSnapshot> =
        withContext(Dispatchers.IO) {
            pokemonDatabaseDao.getFavoritePokemonSnapshots().map(::mapPokemonSnapshotEntityToDomain)
        }

    override suspend fun setFavoritePokemon(name: String) =
        withContext(Dispatchers.IO) {
            pokemonDatabaseDao.insertFavoritePokemon(FavoritePokemon(name))
            Timber.tag("REPO").d("Insert $name as favorite")
            return@withContext
        }


    override suspend fun removeFavoritePokemon(name: String) =
        withContext(Dispatchers.IO) {
            pokemonDatabaseDao.deleteFavouritePokemons(name)
            Timber.tag("REPO").d("Remove $name from favorites")
            return@withContext
        }


    override suspend fun getPokemon(name: String): Pokemon? =
        withContext(Dispatchers.IO) {
            getPokemonFromDB(name) ?: getPokemonFromRemoteApi(name)?.also {
                pokemonDatabaseDao.insertPokemonData(listOf(it))
            }
        }

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
