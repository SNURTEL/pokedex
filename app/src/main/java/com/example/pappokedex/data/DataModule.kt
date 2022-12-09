package com.example.pappokedex.data

import android.content.Context
import androidx.room.Room
import com.example.pappokedex.data.database.PokemonDao
import com.example.pappokedex.data.database.PokemonDatabase
import com.example.pappokedex.data.pokeapi.PokeApi
import com.example.pappokedex.data.pokeapi.PokeApiHelper
import com.example.pappokedex.domain.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideRepo(
        pokeApi: PokeApi,
        pokemonDao: PokemonDao,
    ): PokemonRepository = PokemonRepositoryImp(pokeApi, pokemonDao)

    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase) = pokemonDatabase.pokemonDao()

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            PokemonDatabase::class.java, "pokemons-database"
        ).build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun providePokemonApi(remoteDataSource: PokeApiHelper) = remoteDataSource.getApi()

    @Provides
    @Singleton
    fun provideRemoteDataSource() = PokeApiHelper()
}