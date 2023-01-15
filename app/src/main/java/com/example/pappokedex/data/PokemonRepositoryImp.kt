package com.example.pappokedex.data

import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.entities.FavoritePokemon
import com.example.pappokedex.data.database.mapPokemonEntityToDomain
import com.example.pappokedex.data.database.mapPokemonSnapshotEntityToDomain
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.domain.PokemonSnapshot
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

private const val PAGE_SIZE = 30

class PokemonRepositoryImp @Inject constructor(
    private val pokemonRemoteApi: PokeApi,
    private val pokemonDatabaseDao: PokemonDao
) : PokemonRepository {
    private val snapshotsStateFlow = MutableStateFlow(emptyList<PokemonSnapshot>())

    override val snapshotsFlow: StateFlow<List<PokemonSnapshot>>
        get() = snapshotsStateFlow.asStateFlow()

    private val favouritesStateFlow = MutableStateFlow(emptyList<PokemonSnapshot>())

    override val favouritesFlow: StateFlow<List<PokemonSnapshot>>
        get() = favouritesStateFlow.asStateFlow()

    override suspend fun getAllSnapshots() {
        var pageIndex = 0
        var finalPageReached = false
        while (!finalPageReached) {
            val pokemons = getSnapshotsPage(pageIndex, PAGE_SIZE)
            pageIndex += 1
            if (pokemons != null) {
                snapshotsStateFlow.emit(pokemons)
            } else {
                finalPageReached = true
            }
            Timber.d("$pageIndex, page") // ${pokemons?.map { it.name }}
        }
    }

    override suspend fun getFavoriteSnapshots() =
        withContext(Dispatchers.IO) {
            val favourites = pokemonDatabaseDao
                .getFavoritePokemonSnapshots()
                .map(::mapPokemonSnapshotEntityToDomain)

            favouritesStateFlow.emit(favourites)
        }


    private suspend fun getSnapshotsPage(pageIndex: Int, pageSize: Int): List<PokemonSnapshot>? =
        withContext(Dispatchers.IO) {
            val response = pokemonRemoteApi.getAllPokeResources()

            val startIndex = pageIndex * pageSize
            val results = response.body()?.results ?: return@withContext null
            if (startIndex >= results.size) {
                return@withContext null
            }

            val pokemonResources = results.slice(startIndex until startIndex + pageSize)

            val pokemonSnapshotsFromDb = pokemonDatabaseDao.getPokemonSnapshots().map {
                mapPokemonSnapshotEntityToDomain(it)
            }
            if (startIndex + pageSize != pokemonSnapshotsFromDb.count()) {
                val missingPokemonNames = pokemonResources
                    .map { it.name }
                    .minus(pokemonSnapshotsFromDb.map { it.name }.toSet())

                pokemonDatabaseDao.insertPokemonData(
                    missingPokemonNames.map { pokemonName ->
                        async { getPokemonFromRemoteApi(pokemonName) }
                    }.awaitAll()
                        .also {
                            if (it.contains(null)) {
                                return@withContext null
                            }
                        }.filterNotNull()
                )
            }

            pokemonDatabaseDao.getPokemonSnapshots().map(::mapPokemonSnapshotEntityToDomain)
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
                Timber.tag("REPO").d("Get $name pokemon from API/DB")
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
