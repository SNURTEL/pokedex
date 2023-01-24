package com.example.pappokedex.mock

import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.models.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


fun getMockPokemonResponse(): Response<PokemonModel> {
    val model = PokemonModel(
        name = "eevee",
        height = 123,
        weight = 456,
        abilities = listOf(
            ResourceAbilityModel(
                is_hidden = true,
                slot = 1337,
                ability = NamedApiResourceModel(
                    "UWU",
                    "https://pokeapi.co/api/v2/type/1/"
                )
            )
        ),
        types = listOf(),
        sprites = SpriteUrlsModel(null, null, null, null),
        species = NamedApiResourceModel("sprites", "https://pokeapi.co/api/v2/pokemon-species/132/")
    )

    return Response.success(
        200,
        model
    )
}

class MockPokeApi : PokeApi {
    override suspend fun getPokemon(name: String): Response<PokemonModel> {
        return getMockPokemonResponse()
    }

    override suspend fun getAllPokeResources(): Response<PokemonResourceListModel> {
        return Response.error(500, "Not implemented!".toResponseBody())
//        TODO("Not yet implemented")
    }

    override suspend fun getSpecies(name: String): Response<SpeciesModel> {
        return Response.error(500, "Not implemented!".toResponseBody())
//        TODO("Not yet implemented")
    }

    override suspend fun getAbility(name: String): Response<SingleAbilityModel> {
        return Response.error(500, "Not implemented!".toResponseBody())
//        TODO("Not yet implemented")
    }

}
