package com.example.pappokedex

import com.example.pappokedex.data.PokemonRepositoryImp
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.mock.MockPokeApi
import com.example.pappokedex.mock.MockPokemonDao
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test


class RepositoryTest {
    val api = MockPokeApi()

    @Test
    fun testInsertPokemon() {
        val dao = MockPokemonDao()
        val repo : PokemonRepository = PokemonRepositoryImp(api, dao)

        runBlocking {
            assertNotNull(repo.getPokemon("bulbasaur"))
            assert(dao.inner_pokemons[0].name == "bulbasaur")
        }

    }
}
