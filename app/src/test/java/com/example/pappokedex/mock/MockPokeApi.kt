package com.example.pappokedex.mock

import com.example.pappokedex.data.PokemonRepositoryImp
import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.entities.PokemonEntity
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.models.*
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


fun getMockPokemonResponse(): Response<PokemonModel> {
    val model = PokemonModel(
        name = "eevee",
        height = 69,
        weight = 420,
        abilities = listOf(
            PokemonAbilityModel(
                is_hidden = true,
                slot = 1337,
                ability = NamedApiResourceModel(
                    "UWU",
                    "https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjh7ozansf8AhVW6CoKHVZpBUsQyCl6BAgkEAM&url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DdQw4w9WgXcQ&usg=AOvVaw0aHtehaphMhOCAkCydRLZU"
                )
            )
        ),
        types = listOf(),
        sprites = SpriteUrlsModel(null, null, null, null),
        species = NamedApiResourceModel("sprites", "https://weiti.pl")
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

    override suspend fun getAbility(name: String): Response<AbilityModel> {
        return Response.error(500, "Not implemented!".toResponseBody())
//        TODO("Not yet implemented")
    }

}
