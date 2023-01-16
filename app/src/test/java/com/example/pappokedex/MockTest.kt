package com.example.pappokedex

import com.example.pappokedex.data.PokemonRepositoryImp
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.mock.MockPokeApi
import com.example.pappokedex.mock.MockPokemonDao
import kotlinx.coroutines.runBlocking
import org.junit.Test


class MockTest {
    val api = MockPokeApi()
    val dao = MockPokemonDao()
    val repo : PokemonRepository = PokemonRepositoryImp(api, dao)

    @Test
    fun checkSanity() {
        runBlocking {
            val pokemon = repo.getPokemon("bulbasaur")
            assert(pokemon?.weight == 777)
        }
    }
}
