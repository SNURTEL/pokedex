package com.example.pappokedex

import android.util.Log
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import com.example.pappokedex.data.pokeapi.mapModelToAbility
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
import com.example.pappokedex.data.pokeapi.models.AbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import org.junit.Assert.*

// TODO mock the network API
@OptIn(ExperimentalSerializationApi::class)
class ModelMappersTest {
    private val api = PokeApiHelper.getInstance().create(PokeApi::class.java)
    private val loggerTag = "ModelMapperTest"

    @Test
    fun testPokemonModelMapper() {


        var pokemonModel: PokemonModel
        val abilityModels = mutableListOf<AbilityModel>()
        var speciesModel: SpeciesModel

        runBlocking {
            val pokemonModelResponse = api.getPokemon("bulbasaur")
            if(!pokemonModelResponse.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${pokemonModelResponse.code()}: ${pokemonModelResponse.message()}")
                fail()
            }
            pokemonModel = pokemonModelResponse.body()!!

            val abilityModelResponse = api.getAbility("stench")
            if(!abilityModelResponse.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${abilityModelResponse.code()}: ${abilityModelResponse.message()}")
                fail()
            }
            abilityModels.add(abilityModelResponse.body()!!)

            val speciesModelResponse = api.getSpecies("wormadam")
            if(!speciesModelResponse.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${speciesModelResponse.code()}: ${speciesModelResponse.message()}")
                fail()
            }
            speciesModel = speciesModelResponse.body()!!
        }

        val pokemon = mapModelsToPokemon(
            pokemonModel = pokemonModel,
            speciesModel = speciesModel,
            abilityModels = abilityModels
        )
        return  // put a debugging breakpoint here and inspect the object lol
    }

    @Test
    fun testAbilityModelMapper(){
        var abilityModel: AbilityModel
        runBlocking {
            val abilityModelResponse = api.getAbility("stench")
            if(!abilityModelResponse.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${abilityModelResponse.code()}: ${abilityModelResponse.message()}")
                fail()
            }
            abilityModel = abilityModelResponse.body()!!
        }
        val ability = mapModelToAbility(abilityModel)

        return
    }
}