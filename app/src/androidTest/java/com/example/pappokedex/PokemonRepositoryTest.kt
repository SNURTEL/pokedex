package com.example.pappokedex

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pappokedex.data.PokemonRepositoryImp
import com.example.pappokedex.data.database.PokemonDatabase
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import com.example.pappokedex.data.pokeapi.models.SingleAbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.PokemonResourceListModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking

import org.junit.Test

import retrofit2.Response



class TestApiWrapper(val innerApi: PokeApi) : PokeApi {
    var getPokemonCallCounter = 0;
    var getAbilityCallCounter = 0;
    var getSpeciesCallCounter = 0;
    var getAllResourcesCallCounter = 0;


    override suspend fun getPokemon(name: String): Response<PokemonModel> = runBlocking {
        getPokemonCallCounter++
        return@runBlocking innerApi.getPokemon(name)
    }

    override suspend fun getAbility(name: String): Response<SingleAbilityModel> = runBlocking {
        getAbilityCallCounter++
        return@runBlocking innerApi.getAbility(name)
    }

    override suspend fun getSpecies(name: String): Response<SpeciesModel> = runBlocking {
        getSpeciesCallCounter++
        return@runBlocking innerApi.getSpecies(name)
    }


    override suspend fun getAllPokeResources(): Response<PokemonResourceListModel> = runBlocking {
        getAllResourcesCallCounter++
        innerApi.getAllPokeResources()
    }
}

@HiltAndroidTest
class PokemonRepositoryTest {
    @Test
    fun testSinglePokemonCaching() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        runBlocking {
            val db = Room.databaseBuilder(appContext, PokemonDatabase::class.java, "DB")
                .allowMainThreadQueries().build()
            db.clearAllTables()
            val dao = db.pokemonDao()
            val api = TestApiWrapper(PokeApiHelper().getApi())

            val repo = PokemonRepositoryImp(api, dao)
            val pokemon1 = repo.getPokemon("bulbasaur")
            val pokemon2 = repo.getPokemon("eevee")
            val pokemon3 = repo.getPokemon("bulbasaur")
            assert(api.getPokemonCallCounter == 2)
            assert(pokemon1 == pokemon3)
            return@runBlocking
        }
    }

    @Test
    fun testPartialSnapshotCaching() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        runBlocking {
            val db = Room.databaseBuilder(appContext, PokemonDatabase::class.java, "DB")
                .allowMainThreadQueries().build()
            db.clearAllTables()
            val dao = db.pokemonDao()
            val api = TestApiWrapper(PokeApiHelper().getApi())
            val repo = PokemonRepositoryImp(api, dao)

            // IDs 1, 2 and 3
            repo.getPokemon("bulbasaur")
            repo.getPokemon("ivysaur")
            repo.getPokemon("venusaur")

            repo.getAllSnapshots()
            val snapshots = repo.snapshotsFlow.value
            assert(api.getPokemonCallCounter == snapshots.size)   // no repeated calls, 3 saved
            return@runBlocking
        }
    }

    @Test
    fun testFullSnapshotCaching() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        runBlocking {
            val db = Room.databaseBuilder(appContext, PokemonDatabase::class.java, "DB")
                .allowMainThreadQueries().build()
            db.clearAllTables()
            val dao = db.pokemonDao()
            val api = TestApiWrapper(PokeApiHelper().getApi())
            val repo = PokemonRepositoryImp(api, dao)

            repo.getAllSnapshots()
            val snapshots = repo.snapshotsFlow.value
            val initialCalls = api.getPokemonCallCounter
            repo.getAllSnapshots()
            val newSnapshots = repo.snapshotsFlow.value
            assert(api.getPokemonCallCounter == initialCalls)   // no more calls
            assert(snapshots.count() == newSnapshots.count())

            return@runBlocking
        }
    }
}
