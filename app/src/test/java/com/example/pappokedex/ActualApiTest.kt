package com.example.pappokedex

import android.util.Log
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*


class ActualApiTest {
    private val loggerTag = "ApiCallsTest"
    private val pokeApi = PokeApiHelper().getApi()

    @Test
    fun testApiSinglePokemon() {
        runBlocking {
            val response = pokeApi.getPokemon("bulbasaur")
            if (response.isSuccessful) {
                Log.i(
                    loggerTag,
                    "Success! ${response.body()?.name?.replaceFirstChar(Char::uppercase)} acquired"
                )
            } else {
                Log.d("api", "API call failed (${response.errorBody()}$")
                fail()
            }
        }
    }
}
