package com.example.pappokedex

import android.util.Log
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

// for debugging purposes, mostly safe to ignore
class PokeApiTest {
    val api = PokeApiHelper.getInstance().create(PokeApi::class.java)
    val loggerTag = "PokeApiTest"

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
}