package com.example.pappokedex

import android.util.Log
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import com.example.pappokedex.data.pokeapi.mapModelToAbility
import com.example.pappokedex.data.pokeapi.mapModelsToPokemon
import com.example.pappokedex.data.pokeapi.models.SingleAbilityModel
import com.example.pappokedex.data.pokeapi.models.PokemonModel
import com.example.pappokedex.data.pokeapi.models.SpeciesModel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalSerializationApi::class)
class ModelMappersTest {
    private val api = PokeApiHelper().getApi()
    private val loggerTag = "ModelMapperTest"

    @Test
    fun testPokemonModelMapper() {


        var pokemonModel: PokemonModel
        val abilityModels = mutableListOf<SingleAbilityModel>()
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
        return  
    }

    @Test
    fun testAbilityModelMapper(){
        var abilityModel: SingleAbilityModel
        runBlocking {
            val abilityModelResponse = api.getAbility("stench")
            if(!abilityModelResponse.isSuccessful) {
                Log.e(loggerTag, "FAIL: ${abilityModelResponse.code()}: ${abilityModelResponse.message()}")
                fail()
            }
            abilityModel = abilityModelResponse.body()!!
        }

        return
    }
}
