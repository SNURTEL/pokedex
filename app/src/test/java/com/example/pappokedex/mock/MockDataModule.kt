package com.example.pappokedex.mock

import android.content.Context
import androidx.room.Room
import com.example.pappokedex.data.DataModule
import com.example.pappokedex.data.PokemonRepositoryImp
import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.PokemonDatabase
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import com.example.pappokedex.domain.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton


// move the entire thing to androidTest directory if needed

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object MockDataModule {
    @Provides
    fun provideRepo(
        pokeApi: MockPokeApi,
        pokemonDao: MockPokemonDao,
    ): PokemonRepository = PokemonRepositoryImp(pokeApi, pokemonDao)

    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase) = MockPokemonDao()

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            PokemonDatabase::class.java, "pokemons-database"
        ).build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun providePokemonApi(remoteDataSource: PokeApiHelper) = MockPokeApi()

    @Provides
    @Singleton
    fun provideRemoteDataSource() = PokeApiHelper()
}
