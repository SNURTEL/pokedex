package com.example.pappokedex

import android.util.Log
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test


// will log a warning when https request fails, test itself will be OK
// mostly for debugging purposes
@OptIn(ExperimentalSerializationApi::class)
class DeserializationTest {
    private val api = PokeApiHelper().getApi()
    private val loggerTag = "DeserializationTest"

    @Test
    fun testSinglePokemon() {
        runBlocking {
            val response = api.getPokemon("bulbasaur")
            if(!response.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${response.code()}: ${response.message()}")
            }
        }
    }

    @Test
    fun testSpecies() {
        runBlocking {
            val response = api.getSpecies("wormadam")
            if(!response.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${response.code()}: ${response.message()}")
            }
        }
    }

    @Test
    fun testAbility() {
        runBlocking {
            val response = api.getAbility("stench")
            if(!response.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${response.code()}: ${response.message()}")
            }
        }
    }
}
