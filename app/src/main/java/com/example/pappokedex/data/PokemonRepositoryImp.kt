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
import java.lang.Integer.min
import javax.inject.Inject

private const val PAGE_SIZE = 30
private const val LOG_TAG = "REPO"

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
            Timber.tag(LOG_TAG).d("Fetch page $pageIndex")
            val pokemons = getSnapshotsPage(pageIndex, PAGE_SIZE)
            pageIndex += 1
            if (pokemons != null) {
                snapshotsStateFlow.emit(pokemons)
            } else {
                finalPageReached = true
            }
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

            val pokemonResources = results.slice(startIndex until min(startIndex + pageSize, results.size))

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
                                Timber.tag(LOG_TAG).w("API ERROR AT PAGE $pageIndex: ${it.count({it == null})} NULLS!")
//                                return@withContext null
                            }
                        }.filterNotNull().also {
                            Timber.tag(LOG_TAG).d("Insert page $pageIndex ($it.size) to DB}")
                        }
                )
            }


            pokemonDatabaseDao.getPokemonSnapshots().map(::mapPokemonSnapshotEntityToDomain)

        }

    override suspend fun setFavoritePokemon(name: String) =
        withContext(Dispatchers.IO) {
            pokemonDatabaseDao.insertFavoritePokemon(FavoritePokemon(name))
            Timber.tag(LOG_TAG).d("Insert $name as favorite")
            return@withContext
        }


    override suspend fun removeFavoritePokemon(name: String) =
        withContext(Dispatchers.IO) {
            pokemonDatabaseDao.deleteFavouritePokemons(name)
            Timber.tag(LOG_TAG).d("Remove $name from favorites")
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
            val entity = let { pokemonDatabaseDao.getPokemon(name) } ?: run {
                Timber.tag(LOG_TAG).d("$name not in DB!")
                return@coroutineScope null
            }
            entity.let {
                val pokeAbilities = pokemonDatabaseDao.getPokemonAbilities(name)
                Timber.tag(LOG_TAG).d("Get $name from DB")
                mapPokemonEntityToDomain(it, pokeAbilities)
            }
        }


    private suspend fun getPokemonFromRemoteApi(pokemonName: String): Pokemon? =
        coroutineScope {
            val model =
                getResponseBodyOrNull { pokemonRemoteApi.getPokemon(pokemonName) }
                    ?: run {
                        Timber.tag(LOG_TAG).d("Could not get $pokemonName from API")
                        return@coroutineScope null
                    }

            val abilityModels =
                model.abilities
                    .map { resourceAbilityModel ->
                        async {
                            getResponseBodyOrNull {
                                pokemonRemoteApi.getAbility(resourceAbilityModel.ability.url.split('/').last({!it.isEmpty()}).toInt())
                            }
                        }
                    }
                    .awaitAll()
                    .also {
                        if (it.contains(null)) {
                            Timber.tag(LOG_TAG).d("Got $pokemonName from API, but failed to fetch abilities")
                            return@coroutineScope null
                        }
                    }
                    .filterNotNull()



            val speciesModel = getResponseBodyOrNull { pokemonRemoteApi.getSpecies(model.species.url.split('/').last({!it.isEmpty()}).toInt()) }
                ?: run{
                    Timber.tag(LOG_TAG).d("Got $pokemonName from API, but failed to fetch species")
                    return@coroutineScope null }

            Timber.tag(LOG_TAG).d("Got $pokemonName from API!")
            mapModelsToPokemon(model, speciesModel, model.abilities.map { it.ability.name }, abilityModels)
        }
}

private suspend fun <T> getResponseBodyOrNull(query: suspend () -> Response<T>) = query().body()
